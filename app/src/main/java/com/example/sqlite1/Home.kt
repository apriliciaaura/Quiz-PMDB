package com.example.sqlite1

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.sqlite1.`object`.EmpModelClass
import com.example.sqlite1.helper.MyAdapter
import com.example.sqlite1.model.DatabaseHandler
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {

    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //fungsi yang digunakan untuk mengintent button back ke halaman sebelumnya
        balik.setOnClickListener {
            val intent = Intent(context,MainActivity::class.java)
            startActivity(intent)
        }

    }

    // fungsi yang digunakan untuk menyimpan data pegawai
    fun saveRecord(view: View){
        val id = u_id.text.toString()
        val name = u_name.text.toString()
        val email = u_email.text.toString()
        val address = u_address.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if(id.trim()!="" && name.trim()!="" && email.trim()!=""){
            val status = databaseHandler.addEmployee(EmpModelClass(Integer.parseInt(id),name, email, address))
            if(status > -1){
                Toast.makeText(applicationContext,"record save", Toast.LENGTH_LONG).show()
                u_id.text.clear()
                u_name.text.clear()
                u_email.text.clear()
                u_address.text.clear()
            }
        }else{
            Toast.makeText(applicationContext,"id dan email tidak diperbolehkan kosong", Toast.LENGTH_LONG).show()
        }

    }
    // fungsi yang digunakan untuk membaca data pegawai dari database dan menampilkannya dari listview
    fun viewRecord(view: View){
        // membuat instanisasi databasehandler
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)

        // fungsi yang digunakan untuk memanggil fungsi viewemployee dari databsehandler untuk mengambil data pegawai
        val emp: List<EmpModelClass> = databaseHandler.viewEmployee()
        val empArrayId = Array<String>(emp.size){"0"}
        val empArrayName = Array<String>(emp.size){"null"}
        val empArrayEmail = Array<String>(emp.size){"null"}
        val empArrayAddress = Array<String>(emp.size){"null"}
        var index = 0

        // fungsi yang digunakan setiap data yang didapatkan dari database untuk dimasukkan ke array
        for(e in emp){
            empArrayId[index] = e.userId.toString()
            empArrayName[index] = e.userName
            empArrayEmail[index] = e.userEmail
            empArrayAddress[index] = e.userAddress
            index++
        }

        // fungsi yang digunakan untuk membuat customadapter view UI
        val myListAdapter = MyAdapter(this,empArrayId,empArrayName,empArrayEmail, empArrayAddress)
        listView.adapter = myListAdapter
    }

    // fungsi yang digunakan untuk memperbarui data pegawai sesuai id user
    fun updateRecord(view: View){
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)

        val edtId = dialogView.findViewById(R.id.updateId) as EditText
        val edtName = dialogView.findViewById(R.id.updateName) as EditText
        val edtEmail = dialogView.findViewById(R.id.updateEmail) as EditText
        val edtAddress = dialogView.findViewById(R.id.updateAddress) as EditText

        dialogBuilder.setTitle("Pembaruan data")
        dialogBuilder.setMessage("Isi data dibawah ini")
        dialogBuilder.setPositiveButton("Update", DialogInterface.OnClickListener { _, _ ->

            val updateId = edtId.text.toString()
            val updateName = edtName.text.toString()
            val updateEmail = edtEmail.text.toString()
            val updateAddress = edtAddress.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            if(updateId.trim()!="" && updateName.trim()!="" && updateEmail.trim()!="" && updateAddress.trim()!=""){

                val status = databaseHandler.updateEmployee(EmpModelClass(Integer.parseInt(updateId),updateName, updateEmail, updateAddress))
                if(status > -1){
                    Toast.makeText(applicationContext,"data terupdate", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(applicationContext,"id dan email tidak diperbolehkan kosong", Toast.LENGTH_LONG).show()
            }

        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            // tidak melakukan apa2 :)
        })
        val b = dialogBuilder.create()
        b.show()
    }

    // fungsi yang digunakan untuk menghapus data pegawai berdasarkan id
    fun deleteRecord(view: View){
        //creating AlertDialog for taking user id
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_dialog, null)
        dialogBuilder.setView(dialogView)

        val dltId = dialogView.findViewById(R.id.deleteId) as EditText
        dialogBuilder.setTitle("Hapus data")
        dialogBuilder.setMessage("Masukkan id yang akan dihapus")
        dialogBuilder.setPositiveButton("Hapus", DialogInterface.OnClickListener { _, _ ->

            val deleteId = dltId.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            if(deleteId.trim()!=""){

                val status = databaseHandler.deleteEmployee(EmpModelClass(Integer.parseInt(deleteId),"","", ""))
                if(status > -1){
                    Toast.makeText(applicationContext,"data terhapus", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(applicationContext,"id tidak boleh kosong", Toast.LENGTH_LONG).show()
            }

        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
            // tidak melakukan apa2 :)
        })
        val b = dialogBuilder.create()
        b.show()
    }
}

