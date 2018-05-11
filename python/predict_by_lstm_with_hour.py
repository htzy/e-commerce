# coding=utf-8

# https://github.com/tensorflow/tensorflow/tree/master/tensorflow/contrib/timeseries
# https://github.com/hzy46/TensorFlow-Time-Series-Examples
from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import matplotlib
from os import path
import numpy as np
import tensorflow as tf
from tensorflow.contrib.timeseries.python.timeseries import NumpyReader
from tensorflow.contrib.timeseries.python.timeseries import estimators as ts_estimators
from tensorflow.contrib.timeseries.python.timeseries import model as ts_model

# 使用plt.show()时会弹出图片
matplotlib.use("TkAgg")
from matplotlib import pyplot as plt

# 指定字体
plt.rcParams['font.sans-serif'] = ['simhei']

# 指定数据目录和图片目录
_MODULE_PATH = path.dirname(__file__)
_DATA_FILE = path.join(_MODULE_PATH, "data/hour_table_1_2_1.csv")
# _DATA_FILE = path.join(_MODULE_PATH, "data/multivariate_periods.csv")
# _DATA_FILE = path.join(_MODULE_PATH, "data/tmp1.csv")
_FIGS_DIR = path.join(_MODULE_PATH, "figs/")

# TODO 拟合不够，加训练迭代次数？

# 运行参数
learning_rate = 0.001  # 学习率
training_iters = 2500  # 训练迭代次数
# 用于训练的一个batch中有batch_size个序列，每个序列长度window_size
batch_size = 1
window_size = 24 * 7  # TODO 这里训练数据共8天，把数据全部给进去？
# window_size = 100  # TODO 这里训练数据共8天，把数据全部给进去？

# 网络定义参数
feature_nums = 1  # 列数
# state_size = 128  # state_size=一周
state_size = 256  # state_size=一周       # TODO 表达能力不够强，继续加？？
predictions_size = 24  # 在观察量的基础后向后预测的值：预测一周的数据


class _LSTMModel(ts_model.SequentialTimeSeriesModel):
    """A time series model-building example using an RNNCell."""

    def __init__(self, num_units, num_features, dtype=tf.float32):
        """Initialize/configure the model object.
        Note that we do not start graph building here. Rather, this object is a
        configurable factory for TensorFlow graphs which are run by an Estimator.
        Args:
          num_units: The number of units in the model's LSTMCell.
          num_features: The dimensionality of the time series (features per
            timestep).
          dtype: The floating point data type to use.
        """
        super(_LSTMModel, self).__init__(
            # Pre-register the metrics we'll be outputting (just a mean here).
            train_output_names=["mean"],
            predict_output_names=["mean"],
            num_features=num_features,
            dtype=dtype)
        self._num_units = num_units
        # Filled in by initialize_graph()
        self._lstm_cell = None
        self._lstm_cell_run = None
        self._predict_from_lstm_output = None

    def initialize_graph(self, input_statistics=None):
        """Save templates for components, which can then be used repeatedly.
        This method is called every time a new graph is created. It's safe to start
        adding ops to the current default graph here, but the graph should be
        constructed from scratch.
        Args:
          input_statistics: A math_utils.InputStatistics object.
        """
        super(_LSTMModel, self).initialize_graph(input_statistics=input_statistics)
        self._lstm_cell = tf.nn.rnn_cell.LSTMCell(num_units=self._num_units)
        # Create templates so we don't have to worry about variable reuse.
        self._lstm_cell_run = tf.make_template(
            name_="lstm_cell",
            func_=self._lstm_cell,
            create_scope_now_=True)
        # Transforms LSTM output into mean predictions.
        self._predict_from_lstm_output = tf.make_template(
            name_="predict_from_lstm_output",
            func_=lambda inputs: tf.layers.dense(inputs=inputs, units=self.num_features),
            create_scope_now_=True)

    def get_start_state(self):
        """Return initial state for the time series model."""
        return (
            # Keeps track of the time associated with this state for error checking.
            tf.zeros([], dtype=tf.int64),
            # The previous observation or prediction.
            tf.zeros([self.num_features], dtype=self.dtype),
            # The state of the RNNCell (batch dimension removed since this parent
            # class will broadcast).
            [tf.squeeze(state_element, axis=0)
             for state_element in self._lstm_cell.zero_state(batch_size=1, dtype=self.dtype)])

    def _transform(self, data):
        """Normalize data based on input statistics to encourage stable training."""
        mean, variance = self._input_statistics.overall_feature_moments
        return (data - mean) / variance

    def _de_transform(self, data):
        """Transform data back to the input scale."""
        mean, variance = self._input_statistics.overall_feature_moments
        return data * variance + mean

    def _filtering_step(self, current_times, current_values, state, predictions):
        """Update model state based on observations.
        Note that we don't do much here aside from computing a loss. In this case
        it's easier to update the RNN state in _prediction_step, since that covers
        running the RNN both on observations (from this method) and our own
        predictions. This distinction can be important for probabilistic models,
        where repeatedly predicting without filtering should lead to low-confidence
        predictions.
        Args:
          current_times: A [batch size] integer Tensor.
          current_values: A [batch size, self.num_features] floating point Tensor
            with new observations.
          state: The model's state tuple.
          predictions: The output of the previous `_prediction_step`.
        Returns:
          A tuple of new state and a predictions dictionary updated to include a
          loss (note that we could also return other measures of goodness of fit,
          although only "loss" will be optimized).
        """
        state_from_time, prediction, lstm_state = state
        with tf.control_dependencies([tf.assert_equal(current_times, state_from_time)]):
            transformed_values = self._transform(current_values)
            # Use mean squared error across features for the loss.
            # TODO wocao
            # sess = tf.Session()
            # print(sess.run(transformed_values))
            # print(transformed_values.eval())

            # sess.run(tf.Print(transformed_values, [transformed_values], message="This is transformed_values:"))
            tf.Print(prediction, [prediction], message="This is prediction: ")

            # predictions(?,?,1) transformed_values(1,192)
            predictions["loss"] = tf.reduce_mean((prediction - transformed_values) ** 2, axis=-1)
            # if prediction.shape[0] is not None:
            #     tf.logging.info('shape:%f', prediction.shape[0].value())
            # predictions["loss"] = (prediction + transformed_values)

            # tf.logging.info('[loss] predictions["loss"]: %f', predictions["loss"])
            # tf.Print(predictions["loss"], [predictions["loss"]], message="This is predictions['loss']:")

            # Keep track of the new observation in model state. It won't be run
            # through the LSTM until the next _imputation_step.
            new_state_tuple = (current_times, transformed_values, lstm_state)
        return new_state_tuple, predictions

    def _prediction_step(self, current_times, state):
        """Advance the RNN state using a previous observation or prediction."""
        _, previous_observation_or_prediction, lstm_state = state
        lstm_output, new_lstm_state = self._lstm_cell_run(inputs=previous_observation_or_prediction, state=lstm_state)
        next_prediction = self._predict_from_lstm_output(lstm_output)
        new_state_tuple = (current_times, next_prediction, new_lstm_state)
        return new_state_tuple, {"mean": self._de_transform(next_prediction)}

    def _imputation_step(self, current_times, state):
        """Advance model state across a gap."""
        # Does not do anything special if we're jumping across a gap. More advanced
        # models, especially probabilistic ones, would want a special case that
        # depends on the gap size.
        return state

    def _exogenous_input_step(
            self, current_times, current_exogenous_regressors, state):
        """Update model state based on exogenous regressors."""
        raise NotImplementedError(
            "Exogenous inputs are not implemented for this example.")


def train_and_predict():
    x, y = np.loadtxt(_DATA_FILE, usecols=(0, 1), delimiter=',', unpack=True)

    data = {
        tf.contrib.timeseries.TrainEvalFeatures.TIMES: x,
        tf.contrib.timeseries.TrainEvalFeatures.VALUES: y,
    }

    reader = NumpyReader(data)
    # TODO 这里是否可以修改选取数据的方法，不用随机选取？因为数据的规律可以观察到是一天？
    # 用tf.contrib.timeseries.RandomWindonInputFn将x,y变为batch训练数据，一个batch中有batch_size个随机选取的序列，每个序列的长度为window_size
    train_input_fn = tf.contrib.timeseries.RandomWindowInputFn(reader, batch_size=batch_size, window_size=window_size)

    estimator = ts_estimators.TimeSeriesRegressor(
        model=_LSTMModel(num_features=feature_nums, num_units=state_size),  # state_size=一周
        optimizer=tf.train.AdamOptimizer(learning_rate))

    estimator.train(input_fn=train_input_fn, steps=training_iters)
    evaluation_input_fn = tf.contrib.timeseries.WholeDatasetInputFn(reader)
    evaluation = estimator.evaluate(input_fn=evaluation_input_fn, steps=1)

    # 评估之后开始预测
    (predictions,) = tuple(estimator.predict(
        input_fn=tf.contrib.timeseries.predict_continuation_input_fn(evaluation, steps=predictions_size)))

    observed_times = evaluation["times"][0]
    observations = evaluation["observed"][0, :, :]
    evaluated_times = evaluation["times"][0]
    evaluations = evaluation["mean"][0]
    predicted_times = predictions['times']
    predictions = predictions["mean"]

    return observed_times, observations, evaluated_times, evaluations, predicted_times, predictions


def main(un_used):
    (observed_times, observations, evaluated_times, evaluations, predicted_times, predictions) = train_and_predict()

    plt.figure(figsize=(10, 5))  # 设置图片大小
    plt.ylabel(u"AP接入数")

    # 设置x坐标网格，单位：天
    # x_count/24 + 1，1：保证输出0
    x_count = observed_times.shape[0] + predicted_times.shape[0]
    plt.xticks([i * 24 for i in range(0, int(x_count / 24 + 1))],
               [u'第' + str(i + 1) + u'天' for i in range(0, int(x_count / 24 + 1))],
               rotation=19)
    plt.grid(True)
    # 设置成白线
    plt.axvline(x=x_count / 2, ymin=0, ymax=10000, linestyle="dotted", linewidth=1, color='w')
    # 添加
    observed_lines = plt.plot(observed_times, observations, label=u"观察值", color="k")
    evaluated_lines = plt.plot(evaluated_times, evaluations, label=u"评估值", color="g")
    predicted_lines = plt.plot(predicted_times, predictions, label=u"预测值", color="r")

    plt.legend(handles=[observed_lines[0], evaluated_lines[0], predicted_lines[0]], loc="upper left")
    plt.title(u"基于一月数据进行预测")
    plt.savefig(path.join(_FIGS_DIR, 'predict_by_lstm_with_hour.jpg'))
    plt.show()


if __name__ == "__main__":
    tf.logging.set_verbosity(tf.logging.INFO)
    tf.logging.info("'info' log test...")
    tf.logging.error("'err' log test: %s ", 'err')
    tf.app.run(main=main)
