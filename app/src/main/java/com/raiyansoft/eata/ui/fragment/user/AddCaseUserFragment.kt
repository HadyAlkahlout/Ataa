package com.raiyansoft.eata.ui.fragment.user

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.CursorLoader
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.raiyansoft.eata.adapter.AddImageAdapter
import com.raiyansoft.eata.adapter.SpinnerCategoriesAdapter
import com.raiyansoft.eata.databinding.FragmentAddCaseUserBinding
import com.raiyansoft.eata.model.cases.DataX
import com.raiyansoft.eata.model.cases.Image
import com.raiyansoft.eata.ui.fragment.dialog.AddSuccessDialogFragment
import com.raiyansoft.eata.ui.viewmodel.AddCaseViewModel
import com.raiyansoft.eata.ui.viewmodel.CategoriesViewModel
import com.raiyansoft.eata.ui.viewmodel.DetailsViewModel
import com.raiyansoft.eata.util.Constants
import com.raiyansoft.eata.util.Constants.dialog
import com.raiyansoft.eata.util.Resource
import kotlinx.android.synthetic.main.fragment_add_case_user.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File

class AddCaseUserFragment : Fragment(), AddImageAdapter.CancelClick,AddSuccessDialogFragment.GoFragmentMessage {

    private lateinit var mBinding: FragmentAddCaseUserBinding
    private var ids: Long = 0

    private val viewModel by lazy {
        ViewModelProvider(this)[AddCaseViewModel::class.java]
    }
    private val viewModelCategories by lazy {
        ViewModelProvider(requireActivity())[CategoriesViewModel::class.java]
    }

    private val adapterImage = AddImageAdapter(
        ArrayList(),
        this
    )
    private val adapterSpinner by lazy {
        SpinnerCategoriesAdapter(ArrayList())
    }

    private val viewModelDetails by lazy {
        ViewModelProvider(this)[DetailsViewModel::class.java]
    }


    private var dataDetails: DataX? = null
    private val updateImage = ArrayList<String>()
    val map: MutableMap<String, RequestBody> = HashMap()
    val mapimage = ArrayList<MultipartBody.Part>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentAddCaseUserBinding.inflate(inflater)
        return mBinding.root
    }


    val update by lazy { requireArguments() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
            findNavController().navigateUp()
        }

        loadRecyclerView()

        btnGetImages.setOnClickListener {
            //opening file chooser
            if (adapterImage.data.size < 4) {
                permissionImger()
            } else {
                Snackbar.make(
                    requireView(),
                    "لا يمكنك إضافة أكثر من 4 صور",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }




        viewModel.dataStatusLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            dialog.hide()
                            if (data.status) {
                                    AddSuccessDialogFragment(this).show(childFragmentManager,"")
                            }

                        }
                    }
                    is Resource.Error -> {
                        Constants.showDialog(requireActivity())
                        dialog.hide()

                    }
                    is Resource.Loading -> {
                        Timber.d("onViewCreated-> Resource.Loading")
                        Constants.showDialog(requireActivity())
                    }
                }
            })

        adapterSpinner.content.clear()
        viewModelCategories.getCategories()
        viewModelCategories.dataCategoriesLiveData.observe(
            viewLifecycleOwner,
            Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            if (data.status) {
                                adapterSpinner.content.clear()
                                adapterSpinner.content.addAll(data!!.data.data)
                                adapterSpinner.notifyDataSetChanged()
                            }
                        }
                    }
                    is Resource.Error -> {
                        response.message?.let { message ->

                        }
                    }
                    is Resource.Loading -> {

                    }
                }
            })


        appCompatSpinnerTypeCase.apply {
            adapter = adapterSpinner
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        ids = adapterSpinner.content[p2].id
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }
                }

            viewModelDetails.dataStatusLiveData.observe(viewLifecycleOwner,
                androidx.lifecycle.Observer { response ->
                    Timber.d(" onViewCreated->viewModel")
                    when (response) {
                        is Resource.Success -> {
                            Timber.d(" onViewCreated->Resource.Success")
                            response.data?.let { data ->
                                dialog.hide()

                                if (data.status) {
                                    Snackbar.make(
                                        mBinding.root,
                                        "تم حذف الصورة",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                    viewModelDetails.dataStatusLiveData.value = null
                                    viewModelDetails.status = null
                                }
                            }
                        }
                        is Resource.Error -> {
                            Constants.showDialog(requireActivity())
                            dialog.hide()
                        }
                        is Resource.Loading -> {
                            Timber.d("onViewCreated-> Resource.Loading")
                            Constants.showDialog(requireActivity())

                        }
                    }
                })
        }



        if (update.getString("type", "") == "update") {

            Log.e("tttttt", "update")
            dataDetails = update.getParcelable<DataX>(Constants.DETALIS)

            map["id"] = toRequestBody(dataDetails!!.id.toString())

            addTitleCase.setText(dataDetails!!.title)
            addDesCase.setText(dataDetails!!.details)
            addTxtTotal.setText(dataDetails!!.total)
            rcDataImageAddCase.visibility = View.VISIBLE

            btnSendCase.text = "تعديل الحالة"
            adapterImage.data.addAll(dataDetails!!.images)
            adapterImage.notifyDataSetChanged()
            btnSendCase.setOnClickListener {
                uploadData(0)
            }

        } else {
            btnSendCase.setOnClickListener {
                uploadData(1)
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CODE) {


            val file = data!!.data!!
            adapterImage.data.add(Image(file.toString(), "0"))
            adapterImage.notifyDataSetChanged()


            updateImage.add(file.toString())
            rcDataImageAddCase.visibility = View.VISIBLE

        }

    }


    override fun cancelClick(item: Image, i: Int) {
        if (adapterImage.data.size == 0)
            rcDataImageAddCase.visibility = View.GONE

        adapterImage.data.remove(item)
        adapterImage.notifyDataSetChanged()


        if (update.getString("type", "") == "update") {
            viewModelDetails.deleteCase(item.id, 0)

        }

    }


    private fun loadRecyclerView() {
        rcDataImageAddCase.apply {
            adapter = adapterImage
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }


    private fun uploadData(i: Int) {
        val title = addTitleCase.text.toString()
        val dec = addDesCase.text.toString()
        val total = addTxtTotal.text.toString()
        if (title.isEmpty()) {
            addTitleCase.error = "هذا الحقل مطلوب"
            addTitleCase.requestFocus()
            return
        }
        if (dec.isEmpty()) {
            addDesCase.error = "هذا الحقل مطلوب"
            addDesCase.requestFocus()
            return
        }
        if (total.isEmpty()) {
            addTxtTotal.error = "هذا الحقل مطلوب"
            addTxtTotal.requestFocus()
            return
        }

        if (update.getString("type", "") != "update")
            for ((i, image) in adapterImage.data.withIndex()) {
                val imagefile = File(getRealPathFromURI(Uri.parse(image.image)))
                val reqBody = RequestBody.create("images[$i]".toMediaTypeOrNull(), imagefile)
                val partImage: MultipartBody.Part =
                    MultipartBody.Part.createFormData("images[$i]", imagefile.name, reqBody)
                mapimage.add(partImage)
            }
        else {
            if (updateImage.isNotEmpty()) {
                for ((i, image) in updateImage.withIndex()) {
                    val imagefile = File(getRealPathFromURI(Uri.parse(image)))
                    val reqBody = RequestBody.create("images[$i]".toMediaTypeOrNull(), imagefile)
                    val partImage: MultipartBody.Part =
                        MultipartBody.Part.createFormData("images[$i]", imagefile.name, reqBody)
                    mapimage.add(partImage)
                }
            }
        }

        map["title"] = toRequestBody(title)
        map["details"] = toRequestBody(dec)
        map["total"] = toRequestBody(total)
        map["cat_id"] = toRequestBody(ids.toString())

        viewModel.uploadNewCase(map, mapimage, i)

    }


    private fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader =
            CursorLoader(requireContext(), contentUri, proj, null, null, null)
        val cursor = loader.loadInBackground()
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(column_index)
        cursor.close()
        return result
    }


    private fun toRequestBody(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }

    private val REQUEST_IMAGE_CODE = 1

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/png"
        startActivityForResult(intent, REQUEST_IMAGE_CODE)
    }


    private fun permissionImger() {
        Timber.d(" Add Image")
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            selectImage()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {

                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener {

            }
            .check()
    }

    override fun onClickDo(code: String, total: String) {
        findNavController().navigateUp()
        findNavController().navigateUp()
    }

}