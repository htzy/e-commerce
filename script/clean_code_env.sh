#!/bin/bash
SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do
    TARGET="$(readlink "$SOURCE")"
    if [[ $TARGET == /* ]]; then
        echo "SOURCE '$SOURCE' is an absolute symlink to '$TARGET'"
        SOURCE = "$TARGET"
    else
        DIR="$( dirname "$SOURCE" )"
        echo "SOURCE '$SOURCE' is a relative symlink to '$TARGET' (relative to '$DIR')"
        SOURCE="$DIR/$TARGET" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
    fi
done
echo "SOURCE is '$SOURCE'"
RDIR="$( dirname "$SOURCE" )"
DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
if [ "$DIR" != "$RDIR" ]; then
  echo "DIR '$RDIR' resolves to '$DIR'"
fi
echo "DIR is '$DIR'"

PROJECT_ROOT="$DIR/.."


# 删除idea配置
# 下面的""不能删除！否则在不同目录下执行结果不一样，详细见doc
find "$PROJECT_ROOT" -name "*.iml" | xargs rm -fv

find "$PROJECT_ROOT" -name ".idea" | xargs rm -rfv

# 删除编译后文件
find "$PROJECT_ROOT" -name "target" | xargs rm -rfv
find "$PROJECT_ROOT" -name "out" | xargs rm -rfv
find "$PROJECT_ROOT" -name "build" | xargs rm -rfv
find "$PROJECT_ROOT" -name "classes" | xargs rm -rfv
find "$PROJECT_ROOT" -name "ecommerce-logs" | xargs rm -rfv
find "$PROJECT_ROOT" -name "*.log" | xargs rm -rfv

echo "---------after clean: find project_root -name *.iml----------"
echo | find "$PROJECT_ROOT" -name "*.iml"
