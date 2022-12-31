package com.kikoproject.uwidget.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.sun.misc.BASE64Encoder
import android.sun.security.provider.X509Factory
import android.sun.security.x509.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.muntashirakon.adb.AbsAdbConnectionManager
import java.io.*
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.cert.Certificate
import java.security.cert.CertificateEncodingException
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.spec.EncodedKeySpec
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import java.util.concurrent.TimeUnit


/*
class ADB(private val context: Context) {
    companion object {
        const val MAX_OUTPUT_BUFFER_SIZE = 1024 * 16
        const val OUTPUT_BUFFER_DELAY_MS = 100L

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: ADB? = null
        fun getInstance(context: Context): ADB = instance ?: synchronized(this) {
            instance ?: ADB(context).also { instance = it }
        }
    }

    private val adbPath = "${context.applicationInfo.nativeLibraryDir}/libadb.so"
    private val scriptPath = "${context.getExternalFilesDir(null)}/script.sh"

    */
/**
     * Is the shell ready to handle commands?
     *//*

    private val _started = MutableLiveData(false)
    val started: LiveData<Boolean> = _started

    private var tryingToPair = false

    */
/**
     * Is the shell closed for any reason?
     *//*

    private val _closed = MutableLiveData(false)
    val closed: LiveData<Boolean> = _closed

    */
/**
     * Where shell output is stored
     *//*

    val outputBufferFile: File = File.createTempFile("buffer", ".txt").also {
        it.deleteOnExit()
    }

    */
/**
     * Single shell instance where we can pipe commands to
     *//*

    private var shellProcess: Process? = null

    */
/**
     * Returns the user buffer size if valid, else the default
     *//*

    fun getOutputBufferSize(): Int {
        return MAX_OUTPUT_BUFFER_SIZE
    }

    */
/**
     * Start the ADB server
     *//*

    fun initServer(): Boolean {
        if (_started.value == true || tryingToPair)
            return true

        tryingToPair = true

        val autoShell = true

        if (autoShell) {
            adb(false, listOf("start-server")).waitFor()
            val waitProcess = adb(false, listOf("wait-for-device")).waitFor(2, TimeUnit.MINUTES)
            if (!waitProcess) {
                tryingToPair = false
                return false
            }
        }

        shellProcess = if (autoShell) {
            val argList = if (Build.SUPPORTED_ABIS[0] == "arm64-v8a")
                listOf("-t", "1", "shell")
            else
                listOf("shell")

            adb(true, argList)
        } else {
            shell(true, listOf("sh", "-l"))
        }

        sendToShellProcess("alias adb=\"$adbPath\"")

        if (autoShell)
            sendToShellProcess("echo 'Entered adb shell'")
        else
            sendToShellProcess("echo 'Entered non-adb shell'")

        _started.postValue(true)
        tryingToPair = false

        return true
    }

    */
/**
     * Wait restart the shell once it dies
     *//*

    fun waitForDeathAndReset() {
        while (true) {
            shellProcess?.waitFor()
            _started.postValue(false)
            adb(false, listOf("kill-server")).waitFor()
            Thread.sleep(3_000)
            initServer()
        }
    }

    */
/**
     * Ask the device to pair on Android 11+ devices
     *//*

    fun pair(ip: String): Boolean {
        val pairShell = adb(false, listOf("connect", ip))

        */
/* Sleep to allow shell to catch up *//*

        Thread.sleep(1000)

        */
/* Continue once finished pairing (or 10s elapses) *//*

        pairShell.waitFor(10, TimeUnit.SECONDS)
        pairShell.destroyForcibly().waitFor()
        return pairShell.exitValue() == 0
    }

    */
/**
     * Send a raw ADB command
     *//*

    private fun adb(redirect: Boolean, command: List<String>): Process {
        val commandList = command.toMutableList().also {
            it.add(0, adbPath)
        }
        return shell(redirect, commandList)
    }

    */
/**
     * Send a raw shell command
     *//*

    private fun shell(redirect: Boolean, command: List<String>): Process {
        val processBuilder = ProcessBuilder(command)
            .directory(context.filesDir)
            .apply {
                if (redirect) {
                    redirectErrorStream(true)
                    redirectOutput(outputBufferFile)
                }

                environment().apply {
                    put("HOME", context.filesDir.path)
                    put("TMPDIR", context.cacheDir.path)
                }
            }

        return processBuilder.start()!!
    }

    */
/**
     * Execute a script
     *//*

    fun sendScript(code: String) {
        */
/* Store script locally *//*

        val internalScript = File(scriptPath).apply {
            bufferedWriter().use {
                it.write(code)
            }
            deleteOnExit()
        }

        */
/* Execute the script here *//*

        sendToShellProcess("sh ${internalScript.absolutePath}")
    }

    */
/**
     * Send commands directly to the shell process
     *//*

    fun sendToShellProcess(msg: String) {
        if (shellProcess == null || shellProcess?.outputStream == null)
            return
        PrintStream(shellProcess!!.outputStream!!).apply {
            println(msg)
            flush()
        }
    }
}
*/


class AdbConnectionManager private constructor(context: Context) :
    AbsAdbConnectionManager() {
    private var mPrivateKey: PrivateKey?
    private var mCertificate: Certificate?
    override fun getPrivateKey(): PrivateKey {
        return mPrivateKey!!
    }

    override fun getCertificate(): Certificate {
        return mCertificate!!
    }

    override fun getDeviceName(): String {
        return "MyAwesomeApp"
    }

    companion object {
        private var INSTANCE: AbsAdbConnectionManager? = null

        @Throws(Exception::class)
        fun getInstance(context: Context): AbsAdbConnectionManager? {
            if (INSTANCE == null || INSTANCE?.isConnected != true) {
                INSTANCE = AdbConnectionManager(context)
            }
            return INSTANCE
        }

        @Throws(IOException::class, CertificateException::class)
        private fun readCertificateFromFile(context: Context): Certificate? {
            val certFile = File(context.filesDir, "cert.pem")
            if (!certFile.exists()) return null
            FileInputStream(certFile).use { cert ->
                return CertificateFactory.getInstance(
                    "X.509"
                ).generateCertificate(cert)
            }
        }

        @Throws(CertificateEncodingException::class, IOException::class)
        private fun writeCertificateToFile(context: Context, certificate: Certificate) {
            val certFile = File(context.filesDir, "cert.pem")
            val encoder = BASE64Encoder()
            FileOutputStream(certFile).use { os ->
                os.write(X509Factory.BEGIN_CERT.toByteArray(StandardCharsets.UTF_8))
                os.write('\n'.code)
                encoder.encode(certificate.encoded, os)
                os.write('\n'.code)
                os.write(X509Factory.END_CERT.toByteArray(StandardCharsets.UTF_8))
            }
        }

        @Throws(
            IOException::class,
            NoSuchAlgorithmException::class,
            InvalidKeySpecException::class
        )
        private fun readPrivateKeyFromFile(context: Context): PrivateKey? {
            val privateKeyFile = File(context.filesDir, "private.key")
            if (!privateKeyFile.exists()) return null
            val privKeyBytes = ByteArray(privateKeyFile.length().toInt())
            FileInputStream(privateKeyFile).use { `is` -> `is`.read(privKeyBytes) }
            val keyFactory = KeyFactory.getInstance("RSA")
            val privateKeySpec: EncodedKeySpec = PKCS8EncodedKeySpec(privKeyBytes)
            return keyFactory.generatePrivate(privateKeySpec)
        }

        @Throws(IOException::class)
        private fun writePrivateKeyToFile(context: Context, privateKey: PrivateKey) {
            val privateKeyFile = File(context.filesDir, "private.key")
            FileOutputStream(privateKeyFile).use { os -> os.write(privateKey.encoded) }
        }
    }

    init {
        api = Build.VERSION.SDK_INT
        mPrivateKey = readPrivateKeyFromFile(context)
        mCertificate = readCertificateFromFile(context)
        if (mPrivateKey == null) {
            // Generate a new key pair
            val keySize = 2048
            val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
            keyPairGenerator.initialize(keySize, SecureRandom.getInstance("SHA1PRNG"))
            val generateKeyPair = keyPairGenerator.generateKeyPair()
            val publicKey = generateKeyPair.public
            mPrivateKey = generateKeyPair.private
            // Generate a new certificate
            val subject = "CN=My Awesome App"
            val algorithmName = "SHA512withRSA"
            val expiryDate = System.currentTimeMillis() + 86400000
            val certificateExtensions = CertificateExtensions()
            certificateExtensions["SubjectKeyIdentifier"] = SubjectKeyIdentifierExtension(
                KeyIdentifier(publicKey).identifier
            )
            val x500Name = X500Name(subject)
            val notBefore = Date()
            val notAfter = Date(expiryDate)
            certificateExtensions["PrivateKeyUsage"] =
                PrivateKeyUsageExtension(notBefore, notAfter)
            val certificateValidity = CertificateValidity(notBefore, notAfter)
            val x509CertInfo = X509CertInfo()
            x509CertInfo["version"] = CertificateVersion(2)
            x509CertInfo["serialNumber"] =
                CertificateSerialNumber(Random().nextInt() and Int.MAX_VALUE)
            x509CertInfo["algorithmID"] = CertificateAlgorithmId(AlgorithmId.get(algorithmName))
            x509CertInfo["subject"] = CertificateSubjectName(x500Name)
            x509CertInfo["key"] = CertificateX509Key(publicKey)
            x509CertInfo["validity"] = certificateValidity
            x509CertInfo["issuer"] = CertificateIssuerName(x500Name)
            x509CertInfo["extensions"] = certificateExtensions
            val x509CertImpl = X509CertImpl(x509CertInfo)
            x509CertImpl.sign(mPrivateKey, algorithmName)
            mCertificate = x509CertImpl
            // Write files
            writePrivateKeyToFile(context, mPrivateKey!!)
            writeCertificateToFile(context, mCertificate!!)
        }
    }
}









