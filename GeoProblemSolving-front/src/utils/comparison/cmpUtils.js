import SparkMD5 from 'spark-md5'

const Util = {
  getFileMD5(file, callback) {
    //声明必要的变量
    var fileReader = new FileReader(),

      //文件每块分割2M，计算分割详情
      chunkSize = 2097152,
      chunks = Math.ceil(file.size / chunkSize),
      currentChunk = 0,

      //创建md5对象（基于SparkMD5）
      spark = new SparkMD5()

    //每块文件读取完毕之后的处理
    fileReader.onload = function (e) {
      //每块交由sparkMD5进行计算
      spark.appendBinary(e.target.result)
      currentChunk++

      //如果文件处理完成计算MD5，如果还有分片继续处理
      if (currentChunk < chunks) {
        loadNext()
      } else {
        callback(spark.end())
      }
    }

    //处理单片文件的上传
    function loadNext() {
      var start = currentChunk * chunkSize,
        end = start + chunkSize >= file.size ? file.size : start + chunkSize

      fileReader.readAsBinaryString(file.slice(start, end))
    }

    loadNext()

  }
}

// 获得文件md5
export default Util;