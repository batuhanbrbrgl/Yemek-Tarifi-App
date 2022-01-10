package com.example.yemektariflerisqlite

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.renderscript.ScriptGroup
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.jar.Manifest

class TarifFragment : Fragment() {

    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null

    private lateinit var button: Button;
    private lateinit var imageView: ImageView;
    private lateinit var yemekismiText: EditText;
    private lateinit var yemekmalzemeText: EditText;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tarif, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button = view.findViewById(R.id.button)
        imageView = view.findViewById(R.id.imageView)
        yemekismiText = view.findViewById(R.id.yemekismiText)
        yemekmalzemeText = view.findViewById(R.id.yemekmalzemeText)


        button.setOnClickListener {
            kaydet(it)
        }
        imageView.setOnClickListener {
            gorselSec(it)
        }

        arguments?.let {
            var gelenBilgi = TarifFragmentArgs.fromBundle(it).bilgi

            if (gelenBilgi.equals("menudengeldi")){
                //yeni bir yemek ekle
                yemekismiText.setText("")
                yemekmalzemeText.setText("")
                button.visibility = View.VISIBLE

                val gorselSecmeArkaPlani = BitmapFactory.decodeResource(context?.resources,R.drawable.resim)
                imageView.setImageBitmap(gorselSecmeArkaPlani)

            }else{
                //daha önce oluşturulan yemeğe bak
                button.visibility = View.INVISIBLE

                val secilenId = TarifFragmentArgs.fromBundle(it).id

                context?.let {
                    try {

                        val db = it.openOrCreateDatabase("Yemekler",Context.MODE_PRIVATE,null)
                        val cursor = db.rawQuery("SELECT*FROM yemekler WHERE id = ? ", arrayOf(secilenId.toString()))

                        val yemekIsmiIndex = cursor.getColumnIndex("yemekismi")
                        val yemekMalzemeIndex = cursor.getColumnIndex("yemekmalzemesi")
                        val yemekGorseli = cursor.getColumnIndex("gorsel")

                        while (cursor.moveToNext()){
                            yemekismiText.setText(cursor.getString(yemekIsmiIndex))
                            yemekmalzemeText.setText(cursor.getString(yemekMalzemeIndex))

                            val byteDizisi = cursor.getBlob(yemekGorseli)
                            val bitmap = BitmapFactory.decodeByteArray(byteDizisi,0,byteDizisi.size)
                            imageView.setImageBitmap(bitmap)
                        }
                        cursor.close()

                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }

            }
        }
    }

    fun kaydet(view: View) {
        //SQlite'a Kaydetme
        val yemekIsmi = yemekismiText.text.toString()
        val yemekMalzemeleri = yemekmalzemeText.text.toString()

        if(secilenBitmap !=null){

            val kucukBitmap = kucukBitmapOlustur(secilenBitmap!!, maximumBoyut = 300)

            val outputStream = ByteArrayOutputStream()
            kucukBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val byteDizisi = outputStream.toByteArray()

            try {
                context?.let{
                    val database = it.openOrCreateDatabase("Yemekler", Context.MODE_PRIVATE,null)
                    database.execSQL("CREATE TABLE IF NOT EXISTS yemekler (id INTEGER PRIMARY KEY, yemekismi VARCHAR, yemekmalzemesi VARCHAR, gorsel BLOB)")

                    val sqlString = "INSERT INTO yemekler(yemekismi, yemekmalzemesi,gorsel) VALUES (?, ?, ?)"
                    val statement = database.compileStatement(sqlString)
                    statement.bindString(1, yemekIsmi)
                    statement.bindString(2, yemekMalzemeleri)
                    statement.bindBlob(3, byteDizisi)
                    statement.execute()
                }

            }catch (e:Exception){
                e.printStackTrace()
            }

            val action = TarifFragmentDirections.actionTarifFragmentToListeFragment2()
            Navigation.findNavController(view).navigate(action)

        }

    }

    fun gorselSec(view: View) {

        activity?.let {
            if (ContextCompat.checkSelfPermission(
                    it.applicationContext,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //izin verilmedi, izin istememiz gerekiyor.
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)

            } else {
                //izin zaten verilmiş, tekrar istemeden galeriye git.
                val galeriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent, 2)
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {

            if(grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //izni aldık
                val galeriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent, 2)
            }

        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null ){

            secilenGorsel = data.data

            try{

                context?.let {
                    if(secilenGorsel != null){
                        if (Build.VERSION.SDK_INT >=28){
                         val source = ImageDecoder.createSource(it.contentResolver,secilenGorsel!!)
                         secilenBitmap = ImageDecoder.decodeBitmap(source)
                         imageView.setImageBitmap(secilenBitmap)
                        }else{
                            secilenBitmap = MediaStore.Images.Media.getBitmap(it.contentResolver,secilenGorsel)
                            imageView.setImageBitmap(secilenBitmap)

                        }
                    }
                }




            }catch (e: Exception){
                e.printStackTrace()
            }

        }


        super.onActivityResult(requestCode, resultCode, data)
    }

    fun kucukBitmapOlustur(kullanicinSectigiBitmap: Bitmap, maximumBoyut: Int) : Bitmap {

        var width = kullanicinSectigiBitmap.width
        var height = kullanicinSectigiBitmap.height

        val bitmapOrani : Double = width.toDouble() / height.toDouble()

        if(bitmapOrani > 1){
            //gorselimiz yatay
            width = maximumBoyut
            val kisaltilmisHeight = width / bitmapOrani
            height = kisaltilmisHeight.toInt()
        }else{
            //gorselimiz dikey
            height = maximumBoyut
            val kisaltilmisWidth = height * bitmapOrani
            width = kisaltilmisWidth.toInt()
        }

        return Bitmap.createScaledBitmap(kullanicinSectigiBitmap,width,height,true)

    }

}