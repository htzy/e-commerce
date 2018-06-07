#!/usr/bin/env bash

# 或者直接cp
rm -rf ./../python/*
mkdir ./../python/data
mkdir ./../python/fig
cp -rf ~/bigdata/pycodes/lstm/predict_by_lstm_with_hour.py ./../python/predict_by_lstm_with_hour.py
cp -rf ~/bigdata/pycodes/lstm/data/hour_table_1_2_1.csv ./../python/data/hour_table_1_2_1.csv
cp -rf ~/bigdata/pycodes/lstm/figs/predict_by_lstm_with_hour.jpg ./../python/fig/predict_by_lstm_with_hour.jpg