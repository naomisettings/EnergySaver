package cat.copernic.johan.energysaver.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment() {

    private var listener: DatePickerDialog.OnDateSetListener? = null

    private var initialYear: Int = -1
    private var initialMonth: Int = -1
    private var initialDay: Int = -1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // data
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        // Valor seleccionat inicial, posem que el calendari mostri 1 any enrera de l'actual
        if (initialYear == -1)
            initialYear = year - 1

        if (initialMonth == -1)
            initialMonth = c.get(Calendar.MONTH)

        if (initialDay == -1)
            initialDay = c.get(Calendar.DAY_OF_MONTH)

        // Data seleccionada (valor inicial)
        val datePickerDialog = DatePickerDialog(requireActivity(),
            listener, initialYear,initialMonth,initialDay)

        // data minima i maxima que contrindra el calendari
        c.set(Calendar.YEAR, year - 100)
        datePickerDialog.datePicker.minDate = c.timeInMillis
        c.set(Calendar.YEAR, year)
        datePickerDialog.datePicker.maxDate = c.timeInMillis

        return datePickerDialog
    }

    companion object {
        fun newInstance(listener: DatePickerDialog.OnDateSetListener): DatePickerFragment {
            val fragment = DatePickerFragment()
            fragment.listener = listener
            return fragment
        }

        fun newInstance(listener: DatePickerDialog.OnDateSetListener, year: Int, month: Int, day: Int): DatePickerFragment {
            val fragment = DatePickerFragment()
            fragment.listener = listener
            fragment.initialYear = year
            fragment.initialMonth = month
            fragment.initialDay = day
            return fragment
        }
    }

}