package com.zybooks.transpose

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.core.view.marginTop
import java.security.AccessController.getContext

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
class MainActivity : AppCompatActivity() {

    //decleration of variables
    var noteCounter : Int = 0 //note counter is used to determine which image view to place the next note in - - should be replaced with vector.size once vector of objects is created
    lateinit var notePlace : ImageView //notePlace will be used as a variable to hold the place of the next note to be placed on the scale


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // declare vals for buttons and image views
        val wholenoteIB : ImageButton = findViewById<ImageButton>(R.id.wholenoteIB)
        val halfnoteIB : ImageButton = findViewById<ImageButton>(R.id.halfnoteIB)
        val quarternoteIB : ImageButton = findViewById<ImageButton>(R.id.quarternoteIB)
        val eighthnoteIB : ImageButton = findViewById<ImageButton>(R.id.eighthnoteIB)
        val sixteenthnoteIB : ImageButton = findViewById<ImageButton>(R.id.sixteenthnoteIB)
        val noteoneIV : ImageView = findViewById<ImageView>(R.id.noteoneIV)
        val notetwoIV : ImageView = findViewById<ImageView>(R.id.notetwoIV)
        val notethreeIV : ImageView = findViewById<ImageView>(R.id.notethreeIV)
        val notefourIV : ImageView = findViewById<ImageView>(R.id.notefourIV)
        val uparrowIB : ImageButton = findViewById<ImageButton>(R.id.uparrowIB)
        val downarrowIB : ImageButton = findViewById<ImageButton>(R.id.downarrowIB)

        //function determineIV uses class's note counter to return a place for the next note to be
        fun determineIV() : ImageView {
            when(noteCounter% 4){
                0-> return noteoneIV
                1-> return notetwoIV
                2-> return notethreeIV
                3-> return notefourIV
                else -> return noteoneIV
            }
        }

        //set initial visibility for some image views and buttons to be invisible
        noteoneIV.visibility = View.INVISIBLE
        notetwoIV.visibility = View.INVISIBLE
        notethreeIV.visibility = View.INVISIBLE
        notefourIV.visibility = View.INVISIBLE
        uparrowIB.visibility = View.INVISIBLE
        downarrowIB.visibility = View.INVISIBLE

        //disables invisible buttons so they cant accidentally be pressed
        uparrowIB.isActivated = false
        downarrowIB.isActivated = false
        //creation of on click listeners
        wholenoteIB.setOnClickListener{
            notePlace = determineIV()
            createNote(notePlace,R.drawable.wholenotesmall,uparrowIB,downarrowIB)
        }
        halfnoteIB.setOnClickListener{
            notePlace = determineIV()
            createNote(notePlace,R.drawable.halfnotesmall,uparrowIB,downarrowIB)
        }
        quarternoteIB.setOnClickListener{
            notePlace = determineIV()
            createNote(notePlace,R.drawable.quarternotesmall,uparrowIB,downarrowIB)
        }
        eighthnoteIB.setOnClickListener{
            notePlace = determineIV()
            createNote(notePlace,R.drawable.eighthnotesmall,uparrowIB,downarrowIB)
        }
        sixteenthnoteIB.setOnClickListener{
            notePlace = determineIV()
            createNote(notePlace,R.drawable.sixteenthnotesmall,uparrowIB,downarrowIB)
        }
        uparrowIB.setOnClickListener{
            moveNote(notePlace,R.drawable.uparrowsmall,uparrowIB,downarrowIB)
        }
        downarrowIB.setOnClickListener{
            moveNote(notePlace,R.drawable.downarrowsmall,uparrowIB,downarrowIB)
        }
    }
    fun createNote(currentIV : ImageView, currentImage : Int, uparrow : ImageButton, downarrow : ImageButton) {
        noteCounter++
        uparrow.x = currentIV.x
        downarrow.x=currentIV.x
        uparrow.bottom =currentIV.top
        downarrow.top = currentIV.bottom
        if(currentImage == R.drawable.wholenotesmall) {
            uparrow.bottom= (uparrow.bottom+70)
            currentIV.scaleX = .5.toFloat()
            currentIV.scaleY = .5.toFloat()
            currentIV.y = (currentIV.y + 35)
        }
        else{
            currentIV.scaleX = 1.toFloat()
            currentIV.scaleY = 1.toFloat()
        }
        currentIV.let {
            currentIV.setImageResource(currentImage)
            currentIV.visibility = View.VISIBLE
            uparrow.visibility = View.VISIBLE
            downarrow.visibility = View.VISIBLE
        }
    }
    fun moveNote(currentIV : ImageView,currentImage : Int, uparrow : ImageButton, downarrow : ImageButton){
        if(currentImage == R.drawable.uparrowsmall)
        {
            uparrow.y = uparrow.y -14
            downarrow.y=downarrow.y - 14
            currentIV.y = currentIV.y - 14
        }
        else{
            uparrow.y = uparrow.y +14
            downarrow.y=downarrow.y + 14
            currentIV.y = currentIV.y + 14
        }
    }
}
