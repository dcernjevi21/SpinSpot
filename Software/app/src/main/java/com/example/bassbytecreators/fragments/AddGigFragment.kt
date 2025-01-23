package com.example.bassbytecreators.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bassbytecreators.R
import com.example.bassbytecreators.entities.DJGig
import com.example.bassbytecreators.helpers.AddGigDialogHelper
import com.example.bassbytecreators.api.RetrofitClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class AddGigFragment : Fragment() {

    private var userId: Int = -1
    private lateinit var fabAddGig: FloatingActionButton
    private lateinit var txt1: TextView
    private lateinit var txt2: TextView
    private lateinit var txt3: TextView
    private lateinit var txt4: TextView
    private lateinit var txt5: TextView
    private lateinit var txt6: TextView
    private lateinit var txt7: TextView
    private lateinit var txt10: TextView
    private val sdfDate = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    private val sdfDate2 = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_gig, container, false)

        // Inicijaliziraj UI elemente
        fabAddGig = view.findViewById(R.id.fab_add_gig)
        txt1 = view.findViewById(R.id.textView2)
        txt2 = view.findViewById(R.id.textView3)
        txt3 = view.findViewById(R.id.textView4)
        txt4 = view.findViewById(R.id.textView5)
        txt5 = view.findViewById(R.id.textView6)
        txt10 = view.findViewById(R.id.textView10)
        txt6 = view.findViewById(R.id.textView7)
        txt7 = view.findViewById(R.id.textView8)

        // Postavi listener za FAB
        fabAddGig.setOnClickListener {
            showDialog()
        }

        return view
    }

    private fun showDialog() {
        val newAddGigDialogView = LayoutInflater
            .from(requireContext())
            .inflate(R.layout.add_djgig_dialog, null)
        val dialogHelper = AddGigDialogHelper(newAddGigDialogView)
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(newAddGigDialogView)
            .setTitle(getString(R.string.add_new_gig))
            .setPositiveButton(getString(R.string.add_new_gig)) { _, _ ->
                val newGig = dialogHelper.buildGig()

                // Postavi tekstove
                txt1.text = "${txt1.text}: ${newGig.gigDate}"
                txt2.text = "${txt2.text}: ${newGig.location}"
                txt3.text = "${txt3.text}: ${newGig.gigType}"
                txt4.text = "${txt4.text}: ${newGig.name}"
                txt5.text = "${txt5.text}: ${newGig.gigStartTime}"
                txt10.text = "${txt10.text}: ${newGig.gigEndTime}"
                txt6.text = "${txt6.text}: ${newGig.gigFee}"
                txt7.text = "${txt7.text}: ${newGig.description}"

                try {
                    val parsedDate = sdfDate.parse(newGig.gigDate)
                    val formattedDate = sdfDate2.format(parsedDate!!)
                    newGig.gigDate = formattedDate
                } catch (e: ParseException) {
                    Log.e("AddGigsFragment", "Error parsing date: ${e.message}")
                }
                addNewGig(newGig, userId)
            }
            .show()
        dialogHelper.activateDateTimeListeners()
    }

    private fun addNewGig(newGig: DJGig, userId: Int) {
        RetrofitClient.apiService.addNewGig(newGig, userId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Snackbar.make(requireView(), "Uspješno dodavanje gaža!", Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    Snackbar.make(
                        requireView(),
                        "Greška kod dodavanja gaža: ${response.message()}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Snackbar.make(
                    requireView(),
                    "Greška kod spajanja na server.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })
    }
}
