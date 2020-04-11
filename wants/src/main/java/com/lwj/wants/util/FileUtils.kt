package com.lwj.wants.util

import android.app.Activity
import android.os.Environment
import android.text.TextUtils
import java.io.*
import java.nio.charset.Charset
import android.graphics.Bitmap
import android.text.format.Formatter
import org.json.JSONArray
import org.json.JSONObject
import java.nio.channels.FileChannel


object FileUtils {
    val JSON_SUFFIX = ".json"
    val K = 1024L
    val M = 1024 * K
    val G = 1024 * M
    val HOME_DIRECTORY = "/JOCHI/JoChiStore"
    private val DOWNLOAD_DIRECTORY = "download"
    private val DOWNLOAD_SUPPORTSERVICE = "supportservice"
    private val DOWNLOAD_MISSIONMARKET = "missionmarket"
    private val IMAGE_DIRECTORY = "image"
    private val CACHE_DIRECTORY = "cache"
    private val DATABASE_DIRECTORY = "database"
    private val ERROR_DIRECTORY = "error"
    private val VIDEO_DIRECTORY = "video"
    private val AUDIO_DIRECTORY = "audio"
    private val LOG_DIRECTORY = "log"

    /**
     *  判断外部存储器状态
     */
    fun externalAvailable(): Boolean {
        return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
    }
    /**
     * 获取当前项目的主目录
     *
     * @return
     */
    val homeDirectory: File
        get() {
            val homeDirectory = File(Environment.getExternalStorageDirectory(), HOME_DIRECTORY)
            mkdirs(homeDirectory, true)
            return homeDirectory
        }

    /**
     * 获取当前项目的数据库目录
     *
     * @return
     */
    val databaseDirectory: File
        get() {
            val file = File(homeDirectory, DATABASE_DIRECTORY)
            mkdirs(file, true)
            return file
        }

    /**
     * 获取当前项目的下载目录
     *
     * @return
     */
    val downloadDirectory: File
        get() {
            val downloadDirectory = File(homeDirectory, DOWNLOAD_DIRECTORY)
            mkdirs(downloadDirectory, true)
            return downloadDirectory
        }

    /**
     * 获取当前项目的支撑服务的文件下载目录
     *
     * @return
     */
    val supportServiceDownloadDirectory: File
        get() {
            val downloadDirectory = File(downloadDirectory, DOWNLOAD_SUPPORTSERVICE)
            mkdirs(downloadDirectory, true)
            return downloadDirectory
        }

    /**
     * 获取当前项目的任务超市的文件下载目录
     *
     * @return
     */
    val missionMarketDownloadDirectory: File
        get() {
            val downloadDirectory = File(downloadDirectory, DOWNLOAD_MISSIONMARKET)
            mkdirs(downloadDirectory, true)
            return downloadDirectory
        }

    /**
     * 获取当前项目的图片目录
     *
     * @return
     */
    val imageDirectory: File
        get() {
            val imageDirectory = File(homeDirectory, IMAGE_DIRECTORY)
            mkdirs(imageDirectory, true)
            return imageDirectory
        }

    /**
     * 获取图片的目录
     *
     * @return
     */
    val imageDownloadDirectory: File
        get() {
            val imageDirectory = File(downloadDirectory, IMAGE_DIRECTORY)
            mkdirs(imageDirectory, true)
            return imageDirectory
        }

    /**
     * 获取当前项目的缓存目录
     *
     * @return
     */
    val cacheDirectory: File
        get() {
            val cacheDirectory = File(homeDirectory, CACHE_DIRECTORY)
            mkdirs(cacheDirectory, true)
            return cacheDirectory
        }

    /**
     * 获取当前项目的视频目录
     *
     * @return
     */
    val videoDirectory: File
        get() {
            val videoDirectory = File(cacheDirectory, VIDEO_DIRECTORY)
            mkdirs(videoDirectory, true)
            return videoDirectory
        }

    /**
     * 获取当前项目的音频目录
     *
     * @return
     */
    val audioDirectory: File
        get() {
            val audioDirectory = File(cacheDirectory, AUDIO_DIRECTORY)
            mkdirs(audioDirectory, true)
            return audioDirectory
        }

    /**
     * 获取当前项目的错误目录
     *
     * @return
     */
    val errorDirectory: File
        get() {
            val errorDirectory = File(homeDirectory, ERROR_DIRECTORY)
            mkdirs(errorDirectory, true)
            return errorDirectory
        }

    val logDirectory: File
        get() {
            val logDirectory = File(homeDirectory, LOG_DIRECTORY)
            mkdirs(logDirectory, true)
            return logDirectory
        }

    /**
     * 保存bitmap到本地
     */
    fun bitmapToFile(bitmap: Bitmap, file: File): Boolean {
        return bitmapToFile(bitmap, file, Bitmap.CompressFormat.JPEG)
    }

    fun bitmapToFile(bitmap: Bitmap, file: File, format: Bitmap.CompressFormat): Boolean {
        try {
            val fos = FileOutputStream(file)
            bitmap.compress(format, 100, fos)
            fos.flush()
            fos.close()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }


    /**
     * 创建文件的目录，如果父路径不存在则会自动创建
     *
     * @param file
     * @param isDirectory
     * @return
     */
    fun mkdirs(file: File, isDirectory: Boolean): Boolean {
        var result = false
        try {
            if (isDirectory) {
                if (!file.exists()) {
                    result = file.mkdirs()
                }
            } else {
                val parent = file.parentFile
                if (!parent.exists()) {
                    result = parent.mkdirs()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    /**
     * 删除文件，递归删除
     *
     * @param file
     * @return
     */
    fun delete(file: File): Boolean {
        var result = true
        try {
            if (file.exists()) {
                if (file.isDirectory) {
                    val files = file.listFiles()
                    if (files != null && files.isNotEmpty()) {
                        for (f in files) {
                            result = delete(f) && result
                        }
                    }
                }
                return file.delete() && result
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 删除文件路径，递归删除
     *
     * @param path
     * @return
     */
    fun delete(path: String): Boolean {
        return delete(File(path))
    }

    fun saveFile(path: String, inputStream: InputStream): Boolean {
        return saveFile(File(path), inputStream)
    }

    /**
     * 保存文件
     *
     * @param file        目标文件
     * @param inputStream 源输入流
     * @return
     */
    fun saveFile(file: File, inputStream: InputStream): Boolean {
        var result = false
        var fileOutputStream: FileOutputStream? = null
        try {
            mkdirs(file, false)
            fileOutputStream = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var length: Int = inputStream.read(buffer)
            while (length > 0) {
                fileOutputStream.write(buffer, 0, length)
                length = inputStream.read(buffer)
            }
            fileOutputStream.flush()
            result = true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeStream(fileOutputStream)
            closeStream(inputStream)
        }
        return result
    }

    /**
     * 保存文件
     *
     * @param path   保存路径
     * @param reader 源reader
     * @return
     */
    fun saveFile(path: String, reader: Reader): Boolean {
        return saveFile(File(path), reader)
    }

    /**
     * 保存文件
     *
     * @param file   目标文件
     * @param reader 源reader
     * @return
     */
    fun saveFile(file: File, reader: Reader): Boolean {
        val result = false
        var writer: Writer? = null
        try {
            mkdirs(file, false)
            writer = FileWriter(file)
            val buffer = CharArray(1024)
            var length: Int = reader.read(buffer)
            while (length > 0) {
                writer.write(buffer, 0, length)
                length = reader.read(buffer)
            }
            writer.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeStream(reader)
            closeStream(writer)
        }
        return result
    }

    /**
     * 保存字符串
     *
     * @param file 目标文件
     * @param s    字符串
     * @return
     */
    fun saveString(file: File, s: String): Boolean {
        var result = false
        var fileOutputStream: FileOutputStream? = null
        try {
            mkdirs(file, false)
            fileOutputStream = FileOutputStream(file)
            val bytes = s.toByteArray(Charset.forName("UTF-8"))
            fileOutputStream.write(bytes)
            result = true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeStream(fileOutputStream)
        }
        return result
    }

    /**
     * 加载字符串
     *
     * @param path 目标路径
     * @return
     */
    fun loadString(path: String): String? {
        return loadString(File(path))
    }

    /**
     * 加载字符串
     *
     * @param file 目标文件
     * @return
     */
    fun loadString(file: File): String? {
        var string: String? = null
        var bufferedReader: BufferedReader? = null
        try {
            bufferedReader = BufferedReader(FileReader(file))
            val stringBuilder = StringBuilder()
            var line: String? = bufferedReader.readLine()
            while (!TextUtils.isEmpty(line)) {
                stringBuilder.append(line)
                line = bufferedReader.readLine()
            }
            string = stringBuilder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeStream(bufferedReader)
        }
        return string
    }

    /**
     * 关闭流
     *
     * @param closeable
     */
    fun closeStream(closeable: Closeable?) {
        if (closeable != null) {
            try {
                closeable.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 判断文件路径时候存在
     *
     * @param path 目标路径
     * @return
     */
    fun exists(path: String): Boolean {
        var result = false
        try {
            result = exists(File(path))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    /**
     * 判断文件时候存在
     *
     * @param file 目标文件
     * @return
     */
    fun exists(file: File): Boolean {
        var result = false
        try {
            result = file.exists()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    /**
     * 复制文件
     *
     * @param source 输入路径
     * @param target 输出路径
     */
    fun copy(source: String, target: String) {
        var fileInputStream: FileInputStream? = null
        var fileOutputStream: FileOutputStream? = null
        try {
            fileInputStream = FileInputStream(source)
            fileOutputStream = FileOutputStream(target)
            val buffer = ByteArray(1024)
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeStream(fileInputStream)
            closeStream(fileOutputStream)
        }
    }

    /**
     * 复制文件
     *
     * @param source 输入文件
     * @param target 输出文件
     */
    fun copy(source: File, target: File) {
        var inputChannel : FileChannel ?= null
        var outputChannel :FileChannel? =null
        try {
             inputChannel = FileInputStream(source).channel
             outputChannel = FileOutputStream(target).channel
            outputChannel.transferFrom(inputChannel,0,inputChannel.size())
        }finally {
            inputChannel?.close()
            outputChannel?.close()
        }
    }

    /**
     * 加载字符串
     *
     * @param inputStream
     * @return
     */
    fun loadString(inputStream: InputStream): String? {
        val sb = StringBuilder()
        val br  = BufferedReader(InputStreamReader(inputStream))
        var line =br.readLine()

      while (line!=null){
          sb.append(line)
          line =br.readLine()
      }
        return sb.toString()
    }

    /**
     * 获取文件的大小，递归添加
     *
     * @param path
     * @return
     */
    fun getFileSize(path: String): Long {
        return getFileSize(File(path))
    }

    /**
     * 获取文件的大小，递归添加
     *
     * @param file
     * @return
     */
    fun getFileSize(file: File): Long {
        var size = 0L
        try {
            if (file.exists()) {
                if (file.isDirectory) {
                    val files = file.listFiles()
                    if (files != null && files.isNotEmpty()) {
                        for (f in files) {
                            size += getFileSize(f)
                        }
                    }
                } else {
                    size = file.length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return size
    }

    /**
     * 把文件转换成byte数组
     *
     * @param file
     * @return
     */
    fun fileToBytes(file: File): ByteArray? {
        var bytes: ByteArray? = null
        var fileInputStream: FileInputStream? = null
        var byteArrayOutputStream: ByteArrayOutputStream? = null
        try {
            fileInputStream = FileInputStream(file)
            byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            while (fileInputStream.read(buffer) > 0) {
                byteArrayOutputStream.write(buffer)
            }
            bytes = byteArrayOutputStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeStream(fileInputStream)
            closeStream(byteArrayOutputStream)
        }
        return bytes
    }

    /**
     * byte数组转换file
     *
     * @param bytes
     * @param filename
     * @return
     */
    fun bytesToFile(bytes: ByteArray, filename: String): File? {
        var file: File? = File(cacheDirectory, filename)
        var byteArrayInputStream: ByteArrayInputStream? = null
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file!!)
            byteArrayInputStream = ByteArrayInputStream(bytes)
            val buffer = ByteArray(1024)
            var length: Int = byteArrayInputStream.read(buffer)
            while (length > 0) {
                fileOutputStream.write(buffer, 0, length)
                length = byteArrayInputStream.read(buffer)
            }
            fileOutputStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
            file = null
        } finally {
            closeStream(fileOutputStream)
            closeStream(byteArrayInputStream)
        }
        return file
    }

    fun getSuffix(file: File?): String? {
        if (file == null) {
            return null
        }
        val name = file.name
        val index = name.lastIndexOf('.')
        if (index == -1) {
            return null
        }
        return name.substring(index)
    }

    /**
     * 格式化文件大小
     */
    fun formatSize(activity: Activity, size: Long): String {
        return Formatter.formatFileSize(activity, size)
    }

    /**
     * 获取指定目录内所有文件路径
     * @param dirPath 需要查询的文件目录
     * @param _type 查询类型，比如mp3什么的
     */
    fun getAllFiles(dirPath: String, type: String, directory: Boolean = false, isRecursion: Boolean = false): JSONArray? {
        val f = File(dirPath)
        if (!f.exists()) {//判断路径是否存在
            return null
        }
        val files: Array<out File> = f.listFiles() ?: return null //判断权限
        val fileList = JSONArray()
        for (file in files) {//遍历目录
            if (file.isFile && file.name.endsWith(type)) {
                val name: String = file.name
                val filePath = file.absolutePath//获取文件路径
                val end = name.lastIndexOf('.')
                val fType = if (end != -1) {
                    file.name.substring(end, name.length)//获取文件类型
                } else {
                    null // 无文件类型
                }
                try {
                    val fInfo = JSONObject()
                    fInfo.put("name", name)
                    fInfo.put("type", fType)
                    fInfo.put("path", filePath)
                    fInfo.put("size", getFileSize(filePath))
                    fInfo.put("date", file.lastModified())
                    fileList.put(fInfo)
                } catch (e: Exception) {
                }
            } else if (directory && file.isDirectory) {
                //查询子目录
                val name: String = file.name
                val array: JSONArray? = getAllFiles(file.absolutePath, type, isRecursion)
                try {
                    val fInfo = JSONObject()
                    fInfo.put("name", name)
                    fInfo.put("type", "d")
                    fInfo.put("count", file.list().size)
                    fInfo.put("path", file.absoluteFile)
                    if (isRecursion) {
                        fInfo.put("files", array)
                    }
                    fileList.put(fInfo)
                } catch (e: Exception) {
                }
            } else {
            }
        }
        return fileList
    }
}
