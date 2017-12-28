package com.example.sigh.firstapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import com.example.sigh.firstapp.R.drawable.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*


open class MainActivity : AppCompatActivity() {

    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference
    var myEmail: String? = null
    private var mFirbaseAnalytics: FirebaseAnalytics? = null


    private lateinit var nickName: String
    private var winner = -1
    private var id1 = 0
    private var id2 = 0
    private var counter = 1
    /*private var ldRookNeverMoved = true
    private var rdRookNeverMoved = true
    private var llRookNeverMoved = true
    private var rlRookNeverMoved = true
    private var dKingNeverMoved = true
    private var lKingNeverMoved = true*/
    private var color = Color.TRANSPARENT
    var player = 0
    var previousPlayer = 1

    private lateinit var btn1: ImageButton
    private lateinit var btn2: ImageButton
    private var board = arrayOf("useless", "lRook", "lKnight", "lBishop", "lQueen", "lKing", "lBishop", "lKnight", "lRook",
            "lPawn", "lPawn", "lPawn", "lPawn", "lPawn", "lPawn", "lPawn", "lPawn",
            " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ",
            "dPawn", "dPawn", "dPawn", "dPawn", "dPawn", "dPawn", "dPawn", "dPawn",
            "dRook", "dKnight", "dBishop", "dKing", "dQueen", "dBishop", "dKnight", "dRook")

    var timer = Timer(120000, 1000)
    var c: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFirbaseAnalytics = FirebaseAnalytics.getInstance(this)

        myRef.setValue(null)
        var b: Bundle = intent.extras
        myEmail = b.getString("email")

        var firebaseConnecter = FirebaseConnection()

        firebaseConnecter.incomingCalls()
        firebaseConnecter.playOnline()
        firebaseConnecter.visitTable()
        firebaseConnecter.getWinner()

    }

    private fun getNick() {
        myRef.child("player1")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        try {
                            if(player == 0){
                                upperN.setText(dataSnapshot!!.value.toString())
                            }

                        } catch (ex: Exception) {
                        }
                    }

                    override fun onCancelled(p0: DatabaseError?) {

                    }

                })

        myRef.child("player0")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        try {
                            if(player == 1){
                                upperN.setText(dataSnapshot!!.value.toString())

                            }

                        } catch (ex: Exception) {
                        }
                    }

                    override fun onCancelled(p0: DatabaseError?) {

                    }

                })
    }

    protected fun setChessBoard() {
        if (winner == -1) {
            var movement = Piece(id1, id2, btn1, btn2, player, board, previousPlayer)

            //var movement = Piece(id1, id2, btn1, btn2, player, board, ldRookNeverMoved, rdRookNeverMoved, llRookNeverMoved, rlRookNeverMoved, dKingNeverMoved, lKingNeverMoved, previousPlayer)
            movement.setPiece()

            if (movement.isValidMovement()) {
                /*
                if (id1 == 1) {
                    llRookNeverMoved = false
                } else if (id1 == 8) {
                    rlRookNeverMoved = false
                } else if (id1 == 5) {
                    lKingNeverMoved = false
                } else if (id1 == 64) {
                    ldRookNeverMoved = false
                } else if (id1 == 57) {
                    rdRookNeverMoved = false
                } else if (id1 == 60) {
                    dKingNeverMoved = false
                }

                // Castling
                if (id1 == 5 && id2 == 7) {
                    var btn8 = findViewById(R.id.btn8) as ImageButton
                    var btn6 = findViewById(R.id.btn6) as ImageButton
                    btn8.setImageDrawable(null)
                    btn6.setImageResource(rlt6)
                    board[8] = " "
                    board[6] = "lRook"
                } else if (id1 == 5 && id2 == 3) {
                    var btn1 = findViewById(R.id.button1) as ImageButton
                    var btn4 = findViewById(R.id.btn4) as ImageButton
                    btn1.setImageDrawable(null)
                    btn4.setImageResource(rlt6)
                    board[1] = " "
                    board[4] = "lRook"
                } else if (id1 == 60 && id2 == 58) {
                    var btn57 = findViewById(R.id.btn57) as ImageButton
                    var btn59 = findViewById(R.id.btn59) as ImageButton
                    btn57.setImageDrawable(null)
                    btn59.setImageResource(rdt6)
                    board[57] = " "
                    board[59] = "dRook"
                } else if (id1 == 60 && id2 == 62) {
                    var btn64 = findViewById(R.id.btn64) as ImageButton
                    var btn61 = findViewById(R.id.btn61) as ImageButton
                    btn64.setImageDrawable(null)
                    btn61.setImageResource(rdt6)
                    board[64] = " "
                    board[61] = "dRook"
                }

                */
                board[id2] = board[id1]
                board[id1] = " "

                p1.text = convertToStep(id1) + "->" + convertToStep(id2)

                var ids: String = id1.toString() + "," + id2.toString() + "," + player.toString()
                myRef.child("PlayOnline").child("setPiece").setValue(ids)
                checkWin()


            }
        }

    }

    fun onRestart(view: View) {
        restart()
    }

    fun restart() {

        val i = baseContext.packageManager
                .getLaunchIntentForPackage(baseContext.packageName)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
    }

    protected fun checkWin() {
        var isLKing = false
        var isDKing = false
        for (i in 1..board.size - 1) {
            if (board[i].equals("lKing")) {
                isLKing = true
            } else if (board[i].equals("dKing")) {
                isDKing = true
            }
        }

        if (!isLKing) {
            winner = 1
            myRef.child("Winner").setValue("d")
        } else if (!isDKing) {
            winner = 0
            myRef.child("Winner").setValue("l")
        }
    }

    fun onClick(view: View) {
        var btn = view as ImageButton
        var idGetter = ButtonGetter()
        var id: Int = idGetter.getButtonID(view, btn)

        if (counter == 1) {
            id1 = id
            btn1 = btn
            val background = btn1.background
            if (background is ColorDrawable) {
                color = background.color
            }
            btn1.setBackgroundColor(Color.GRAY)
            counter = 2

        } else if (counter == 2) {
            id2 = id
            btn2 = btn
            btn1.setBackgroundColor(color)
            counter = 1
        }


        if (id1 != 0 && id2 != 0) {
            setChessBoard()
            id1 = 0
            id2 = 0
        }


    }

    fun buRejectEvent(view: View) {
        myRef.child("BoardAccessibility").setValue("N")
        restart()
    }

    fun buRequestEvent(view: View) {
        var userEmail = etemail.text.toString()
        myRef.child("Users").child(splitString(userEmail)).child("isConnected").push().setValue(myEmail)
        player = 0
        myRef.child("player"+player.toString()).setValue(nickName)

        getNick()

        tv.setText("Current Side: Light")
    }

    fun buAcceptEvent(view: View) {
        var userEmail = etemail.text.toString()
        myRef.child("Users").child(splitString(userEmail)).child("isConnected").push().setValue(myEmail)
        player = 1
        myRef.child("player"+player.toString()).setValue(nickName)


        upperP.setImageResource(h1)
        lowerP.setImageResource(d3)

        myRef.child("BoardAccessibility").setValue("Y")
        getNick()

        tv.setText("Current Side: Light")

    }

    @SuppressLint("SetTextI18n")
    fun setOpponentsMove(move: String) {
        var idGetter = ButtonGetter()
        var btn1: ImageButton = idGetter.IdToButton(move.split(",")[0].toInt())
        var btn2: ImageButton = idGetter.IdToButton(move.split(",")[1].toInt())

        btn1.setImageDrawable(null)


        when (board[move.split(",")[0].toInt()]) {
            "dRook" -> btn2.setImageResource(rdt6)
            "dKnight" -> btn2.setImageResource(ndt6)
            "dQueen" -> btn2.setImageResource(qdt6)
            "dKing" -> btn2.setImageResource(kdt6)
            "dBishop" -> btn2.setImageResource(bdt6)
            "dPawn" -> btn2.setImageResource(pdt6)
            "lRook" -> btn2.setImageResource(rlt6)
            "lKnight" -> btn2.setImageResource(nlt6)
            "lQueen" -> btn2.setImageResource(qlt6)
            "lKing" -> btn2.setImageResource(klt6)
            "lBishop" -> btn2.setImageResource(blt6)
            "lPawn" -> btn2.setImageResource(plt6)
        }

        /*
        //opponent castling
        if (move.split(",")[0].toInt() == 5 && move.split(",")[1].toInt() == 7) {
            var btn8 = findViewById(R.id.btn8) as ImageButton
            var btn6 = findViewById(R.id.btn6) as ImageButton
            btn8.setImageDrawable(null)
            btn6.setImageResource(rlt6)
            board[8] = " "
            board[6] = "lRook"
        } else if (move.split(",")[0].toInt() == 5 && move.split(",")[1].toInt() == 3) {
            var btn1 = findViewById(R.id.button1) as ImageButton
            var btn4 = findViewById(R.id.btn4) as ImageButton
            btn1.setImageDrawable(null)
            btn4.setImageResource(rlt6)
            board[1] = " "
            board[4] = "lRook"
        } else if (move.split(",")[0].toInt() == 60 && move.split(",")[1].toInt() == 58) {
            var btn57 = findViewById(R.id.btn57) as ImageButton
            var btn59 = findViewById(R.id.btn59) as ImageButton
            btn57.setImageDrawable(null)
            btn59.setImageResource(rdt6)
            board[57] = " "
            board[59] = "dRook"
        } else if (move.split(",")[0].toInt() == 60 && move.split(",")[1].toInt() == 62) {
            var btn64 = findViewById(R.id.btn64) as ImageButton
            var btn61 = findViewById(R.id.btn61) as ImageButton
            btn64.setImageDrawable(null)
            btn61.setImageResource(rdt6)
            board[64] = " "
            board[61] = "dRook"
        }
*/
        board[move.split(",")[1].toInt()] = board[move.split(",")[0].toInt()]
        board[move.split(",")[0].toInt()] = " "

        p2.text = convertToStep(move.split(",")[0].toInt()) + "->" + convertToStep(move.split(",")[1].toInt())
    }

    fun splitString(str: String): String {
        var split = str.split("@")
        return split[0]
    }

    private fun convertToStep(id: Int): String {
        val step = arrayOf("useless", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8",
                "b8", "b7", "b6", "b5", "b4", "b3", "b2", "b1",
                "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8",
                "d8", "d7", "d6", "d5", "d4", "d3", "d2", "d1",
                "e1", "e2", "e3", "e4", "e5", "e6", "e7", "e8",
                "f8", "f7", "f6", "f5", "f4", "f3", "f2", "f1",
                "g1", "g2", "g3", "g4", "g5", "g6", "g7", "g8",
                "h8", "h7", "h6", "h5", "h4", "h3", "h2", "h1")

        return step[id]
    }

    fun userNameOnClick(view: View){
        nickName = userName.text.toString()
        if(!nickName.equals("")){
            lowerN.setText(nickName)
            etemail.visibility = View.VISIBLE
            request.visibility = View.VISIBLE
            userName.visibility = View.INVISIBLE
            nameConfirm.visibility = View.INVISIBLE

        }else{
            Toast.makeText(applicationContext, "Please Enter Your Nick Name", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onBackPressed() {}

    inner class Timer(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() {
            if (previousPlayer == player) {
                restart.visibility = View.VISIBLE
                Toast.makeText(applicationContext, "Your opponent left", Toast.LENGTH_LONG).show()
            } else {
                restart()
            }
        }

        override fun onTick(l: Long) {
            c++
            tvTimer.setText((120-c).toString())
            when (120 - c) {
                90 -> Toast.makeText(applicationContext, "90s left! Time waits for no one!", Toast.LENGTH_SHORT).show()
                60 -> Toast.makeText(applicationContext, "60s left! Time is money friend!", Toast.LENGTH_SHORT).show()
                30 -> Toast.makeText(applicationContext, "30s left! Time runs out on me!", Toast.LENGTH_SHORT).show()
                15 -> Toast.makeText(applicationContext, "15s left! There is little time!", Toast.LENGTH_SHORT).show()
                5 -> Toast.makeText(applicationContext, "5s left! I must choose soon!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class ButtonGetter {
        fun getButtonID(view: View, btn: ImageButton): Int{

            var id: Int = 0

            when (btn.id) {
                R.id.button1 -> id = 1
                R.id.button2 -> id = 2
                R.id.btn3 -> id = 3
                R.id.btn4 -> id = 4
                R.id.btn5 -> id = 5
                R.id.btn6 -> id = 6
                R.id.btn7 -> id = 7
                R.id.btn8 -> id = 8
                R.id.btn9 -> id = 9
                R.id.btn10 -> id = 10
                R.id.btn11 -> id = 11
                R.id.btn12 -> id = 12
                R.id.btn13 -> id = 13
                R.id.btn14 -> id = 14
                R.id.btn15 -> id = 15
                R.id.btn16 -> id = 16
                R.id.btn17 -> id = 17
                R.id.btn18 -> id = 18
                R.id.btn19 -> id = 19
                R.id.btn20 -> id = 20
                R.id.btn21 -> id = 21
                R.id.btn22 -> id = 22
                R.id.btn23 -> id = 23
                R.id.btn24 -> id = 24
                R.id.btn25 -> id = 25
                R.id.btn26 -> id = 26
                R.id.btn27 -> id = 27
                R.id.btn28 -> id = 28
                R.id.btn29 -> id = 29
                R.id.btn30 -> id = 30
                R.id.btn31 -> id = 31
                R.id.btn32 -> id = 32
                R.id.btn33 -> id = 33
                R.id.btn34 -> id = 34
                R.id.btn35 -> id = 35
                R.id.btn36 -> id = 36
                R.id.btn37 -> id = 37
                R.id.btn38 -> id = 38
                R.id.btn39 -> id = 39
                R.id.btn40 -> id = 40
                R.id.btn41 -> id = 41
                R.id.btn42 -> id = 42
                R.id.btn43 -> id = 43
                R.id.btn44 -> id = 44
                R.id.btn45 -> id = 45
                R.id.btn46 -> id = 46
                R.id.btn47 -> id = 47
                R.id.btn48 -> id = 48
                R.id.btn49 -> id = 49
                R.id.btn50 -> id = 50
                R.id.btn51 -> id = 51
                R.id.btn52 -> id = 52
                R.id.btn53 -> id = 53
                R.id.btn54 -> id = 54
                R.id.btn55 -> id = 55
                R.id.btn56 -> id = 56
                R.id.btn57 -> id = 57
                R.id.btn58 -> id = 58
                R.id.btn59 -> id = 59
                R.id.btn60 -> id = 60
                R.id.btn61 -> id = 61
                R.id.btn62 -> id = 62
                R.id.btn63 -> id = 63
                R.id.btn64 -> id = 64
            }

            return id
        }

        fun IdToButton(id: Int): ImageButton {
            var btn: ImageButton? = null
            if(player == 0){
                when (id) {
                    1 -> btn = tableLayout1.button1
                    2 -> btn = tableLayout1.button2
                    3 -> btn = tableLayout1.btn3
                    4 -> btn = tableLayout1.btn4
                    5 -> btn = tableLayout1.btn5
                    6 -> btn = tableLayout1.btn6
                    7 -> btn = tableLayout1.btn7
                    8 -> btn = tableLayout1.btn8
                    9 -> btn = tableLayout1.btn9
                    10 -> btn = tableLayout1.btn10
                    11 -> btn = tableLayout1.btn11
                    12 -> btn = tableLayout1.btn12
                    13 -> btn = tableLayout1.btn13
                    14 -> btn = tableLayout1.btn14
                    15 -> btn = tableLayout1.btn15
                    16 -> btn = tableLayout1.btn16
                    17 -> btn = tableLayout1.btn17
                    18 -> btn = tableLayout1.btn18
                    19 -> btn = tableLayout1.btn19
                    20 -> btn = tableLayout1.btn20
                    21 -> btn = tableLayout1.btn21
                    22 -> btn = tableLayout1.btn22
                    23 -> btn = tableLayout1.btn23
                    24 -> btn = tableLayout1.btn24
                    25 -> btn = tableLayout1.btn25
                    26 -> btn = tableLayout1.btn26
                    27 -> btn = tableLayout1.btn27
                    28 -> btn = tableLayout1.btn28
                    29 -> btn = tableLayout1.btn29
                    30 -> btn = tableLayout1.btn30
                    31 -> btn = tableLayout1.btn31
                    32 -> btn = tableLayout1.btn32
                    33 -> btn = tableLayout1.btn33
                    34 -> btn = tableLayout1.btn34
                    35 -> btn = tableLayout1.btn35
                    36 -> btn = tableLayout1.btn36
                    37 -> btn = tableLayout1.btn37
                    38 -> btn = tableLayout1.btn38
                    39 -> btn = tableLayout1.btn39
                    40 -> btn = tableLayout1.btn40
                    41 -> btn = tableLayout1.btn41
                    42 -> btn = tableLayout1.btn42
                    43 -> btn = tableLayout1.btn43
                    44 -> btn = tableLayout1.btn44
                    45 -> btn = tableLayout1.btn45
                    46 -> btn = tableLayout1.btn46
                    47 -> btn = tableLayout1.btn47
                    48 -> btn = tableLayout1.btn48
                    49 -> btn = tableLayout1.btn49
                    50 -> btn = tableLayout1.btn50
                    51 -> btn = tableLayout1.btn51
                    52 -> btn = tableLayout1.btn52
                    53 -> btn = tableLayout1.btn53
                    54 -> btn = tableLayout1.btn54
                    55 -> btn = tableLayout1.btn55
                    56 -> btn = tableLayout1.btn56
                    57 -> btn = tableLayout1.btn57
                    58 -> btn = tableLayout1.btn58
                    59 -> btn = tableLayout1.btn59
                    60 -> btn = tableLayout1.btn60
                    61 -> btn = tableLayout1.btn61
                    62 -> btn = tableLayout1.btn62
                    63 -> btn = tableLayout1.btn63
                    64 -> btn = tableLayout1.btn64
                }
            }else{
                when (id) {
                    1 -> btn = button1
                    2 -> btn = button2
                    3 -> btn = btn3
                    4 -> btn = btn4
                    5 -> btn = btn5
                    6 -> btn = btn6
                    7 -> btn = btn7
                    8 -> btn = btn8
                    9 -> btn = btn9
                    10 -> btn = btn10
                    11 -> btn = btn11
                    12 -> btn = btn12
                    13 -> btn = btn13
                    14 -> btn = btn14
                    15 -> btn = btn15
                    16 -> btn = btn16
                    17 -> btn = btn17
                    18 -> btn = btn18
                    19 -> btn = btn19
                    20 -> btn = btn20
                    21 -> btn = btn21
                    22 -> btn = btn22
                    23 -> btn = btn23
                    24 -> btn = btn24
                    25 -> btn = btn25
                    26 -> btn = btn26
                    27 -> btn = btn27
                    28 -> btn = btn28
                    29 -> btn = btn29
                    30 -> btn = btn30
                    31 -> btn = btn31
                    32 -> btn = btn32
                    33 -> btn = btn33
                    34 -> btn = btn34
                    35 -> btn = btn35
                    36 -> btn = btn36
                    37 -> btn = btn37
                    38 -> btn = btn38
                    39 -> btn = btn39
                    40 -> btn = btn40
                    41 -> btn = btn41
                    42 -> btn = btn42
                    43 -> btn = btn43
                    44 -> btn = btn44
                    45 -> btn = btn45
                    46 -> btn = btn46
                    47 -> btn = btn47
                    48 -> btn = btn48
                    49 -> btn = btn49
                    50 -> btn = btn50
                    51 -> btn = btn51
                    52 -> btn = btn52
                    53 -> btn = btn53
                    54 -> btn = btn54
                    55 -> btn = btn55
                    56 -> btn = btn56
                    57 -> btn = btn57
                    58 -> btn = btn58
                    59 -> btn = btn59
                    60 -> btn = btn60
                    61 -> btn = btn61
                    62 -> btn = btn62
                    63 -> btn = btn63
                    64 -> btn = btn64
                }
            }

            return btn!!
        }
    }

    inner class FirebaseConnection{

        fun getWinner() {
            myRef.child("Winner")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot?) {
                            try {
                                if (dataSnapshot!!.value.toString().equals("l")) {
                                    tvWinner.setText("Winner: Light")
                                    tvWinner.setTextColor(Color.WHITE)
                                    tvWinner.visibility = View.VISIBLE
                                    restart.visibility = View.VISIBLE
                                    if (player == 0) {
                                        tableLayout1.visibility = View.INVISIBLE
                                    } else if (player == 1) {
                                        tableLayout2.visibility = View.INVISIBLE
                                    }
                                    tvTimer.visibility = View.INVISIBLE
                                    p1.visibility = View.INVISIBLE
                                    p2.visibility = View.INVISIBLE
                                    tv.visibility = View.INVISIBLE

                                    timer.cancel()
                                } else if (dataSnapshot!!.value.toString().equals("d")) {
                                    tvWinner.setText("Winner: Dark")
                                    tvWinner.setTextColor(Color.WHITE)
                                    tvWinner.visibility = View.VISIBLE
                                    restart.visibility = View.VISIBLE
                                    if (player == 0) {
                                        tableLayout1.visibility = View.INVISIBLE
                                    } else if (player == 1) {
                                        tableLayout2.visibility = View.INVISIBLE
                                    }
                                    tvTimer.visibility = View.INVISIBLE
                                    p1.visibility = View.INVISIBLE
                                    p2.visibility = View.INVISIBLE
                                    tv.visibility = View.INVISIBLE

                                    timer.cancel()
                                }
                            } catch (ex: Exception) {
                            }
                        }

                        override fun onCancelled(p0: DatabaseError?) {

                        }

                    })
        }

        fun incomingCalls() {
            myRef.child("Users").child(splitString(myEmail!!)).child("isConnected")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot?) {
                            try {
                                val td = dataSnapshot!!.value as HashMap<String, Any>
                                if (td != null) {
                                    val value: String
                                    for (key in td.keys) {
                                        value = td[key] as String
                                        if (!value.equals(myEmail)) {
                                            etemail.setText(value)
                                            accept.visibility = View.VISIBLE
                                            reject.visibility = View.VISIBLE

                                            myRef.child("Users").child(splitString(myEmail!!)).child("isConnected").setValue(true)
                                        } else {
                                            Toast.makeText(applicationContext, "You cannot invite yourself", Toast.LENGTH_SHORT).show()
                                        }
                                        break
                                    }

                                }

                            } catch (ex: Exception) {
                            }
                        }

                        override fun onCancelled(p0: DatabaseError?) {

                        }

                    })
        }

        fun playOnline() {
            myRef.child("PlayOnline").child("setPiece")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot?) {
                            try {

                                timer.cancel()
                                c = 0
                                val move: String = dataSnapshot?.value.toString()
                                previousPlayer = move.split(",")[2].toInt()


                                timer.start()

                                if (previousPlayer == 1) {
                                    tv.setText("Current Side: Light")
                                } else tv.setText("Current Side: Dark")


                                if (previousPlayer != player && player != -1) {
                                    setOpponentsMove(move)
                                }

                            } catch (ex: Exception) {
                            }
                        }

                        override fun onCancelled(p0: DatabaseError?) {

                        }

                    })
        }

        fun visitTable() {
            myRef.child("BoardAccessibility")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot?) {
                            try {
                                if (dataSnapshot!!.value.toString().equals("Y")) {
                                    if (player == 0) {
                                        tableLayout1.visibility = View.VISIBLE
                                    } else if (player == 1) {
                                        tableLayout2.visibility = View.VISIBLE
                                    }
                                    lowerN.visibility = View.VISIBLE
                                    upperP.visibility = View.VISIBLE
                                    lowerP.visibility = View.VISIBLE
                                    reject.visibility = View.INVISIBLE
                                    accept.visibility = View.INVISIBLE
                                    request.visibility = View.INVISIBLE
                                    tv.visibility = View.VISIBLE
                                    etemail.visibility = View.INVISIBLE
                                    upperN.visibility = View.VISIBLE
                                    lowerN.visibility = View.VISIBLE

                                }else if(dataSnapshot!!.value.toString().equals("N")){
                                    Toast.makeText(applicationContext, "Rejected", Toast.LENGTH_SHORT).show()
                                }
                            } catch (ex: Exception) {
                            }
                        }

                        override fun onCancelled(p0: DatabaseError?) {

                        }

                    })
        }
    }

}
