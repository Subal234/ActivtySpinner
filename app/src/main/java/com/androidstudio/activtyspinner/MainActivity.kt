package com.androidstudio.activtyspinner

import android.R
import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.androidstudio.activtyspinner.databinding.ActivityMainBinding
import com.androidstudio.activtyspinner.databinding.EditBtnDialogBinding
import com.androidstudio.activtyspinner.databinding.FabBtnDialogBinding
import com.androidstudio.activtyspinner.databinding.ItemBaseAdapterBinding

class MainActivity : AppCompatActivity(),ClickInterface {
    lateinit var binding:ActivityMainBinding
    lateinit var spinner: Spinner
    lateinit var binding1:ItemBaseAdapterBinding
    lateinit var spinnerAdapter: ArrayAdapter<String>
    lateinit var adapter: UserListAdapter
    var position=0

    var spinnerArray= arrayListOf<String>()
    var userArray= ArrayList<UserModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner = binding.spinner

        adapter = UserListAdapter(userArray,this)
        spinnerAdapter = ArrayAdapter(this, R.layout.simple_list_item_1, spinnerArray)
        binding.spinner.adapter = spinnerAdapter
        binding.lvListView.adapter = adapter

        binding.fabbtn.setOnClickListener {
            var dialog = Dialog(this)
            var dialogBinding = FabBtnDialogBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialog.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )

            dialogBinding.btnAdd1.setOnClickListener {
                if (dialogBinding.etItemName.text.toString().trim().isNullOrEmpty()) {
                    dialogBinding.etItemName.error = "Enter Item"
                    Toast.makeText(this, "Enter Item !!!", Toast.LENGTH_LONG).show()
                } else {
                    spinnerArray.add(dialogBinding.etItemName.text.toString())
                    spinnerAdapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
        binding.spinner.setOnItemSelectedListener(object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                position = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                position = -1
            }
        })

        binding.btnAdd2.setOnClickListener {
            if (binding.spinner.toString().trim().isNullOrEmpty()) {
                Toast.makeText(this, "Enter Spinner Item", Toast.LENGTH_SHORT).show()
            } else {
                userArray.add(UserModel(spinnerArray[position]))
                spinnerAdapter.notifyDataSetChanged()
            }
        }

    }

    override fun editClick(position: Int) {
        var dialog = Dialog(this)
        var dialogBinding = EditBtnDialogBinding.inflate(layoutInflater)

        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialogBinding.etName.setText(userArray[position].name)
        dialogBinding.btnUpdate.setOnClickListener {
            if(dialogBinding.etName.text.toString().isNullOrEmpty()){
                dialogBinding.etName.error = "Enter Name"
            }
            else {
                userArray[position]=(UserModel(dialogBinding.etName.text.toString()))
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        }

        dialog.show()

    }

    override fun deleteClick(position: Int) {
        var alertDialog= AlertDialog.Builder(this)
        alertDialog.setTitle("Delete Item")
        alertDialog.setMessage("Do you want to delete the item?")
        alertDialog.setCancelable(false)
        alertDialog.setNegativeButton("No"){_,_->
            alertDialog.setCancelable(true)
        }
        alertDialog.setPositiveButton("Yes"){_,_->
            Toast.makeText(this, "The item is deleted", Toast.LENGTH_SHORT).show()
            userArray.removeAt(position)
            adapter.notifyDataSetChanged()
        }
        alertDialog.show()

    }

    override fun addCounter(position: Int) {
        userArray[position].qty = (userArray[position].qty?:0)+1
        adapter.notifyDataSetChanged()
    }

    override fun removeCounter(position: Int) {
        if(userArray[position].qty!! <= 1){
            userArray.removeAt(position)
            adapter.notifyDataSetChanged()
        }
        else{
            userArray[position].qty = (userArray[position].qty?:0)-1
            adapter.notifyDataSetChanged()
        }
    }
}