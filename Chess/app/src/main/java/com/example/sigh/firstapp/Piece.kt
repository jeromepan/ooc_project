package com.example.sigh.firstapp

import android.widget.ImageButton

class Piece(var id1: Int, var id2: Int, var btn1: ImageButton, var btn2: ImageButton, var player: Int, var board: Array<String>, var previousPlayer: Int) {
    private var checkValidity = false
    var id11 = -1
    var id12 = -1
    var id21 = -1
    var id22 = -1
    lateinit var theBoard: Array<Array<String>>

    fun isValidMovement(): Boolean {
        return checkValidity
    }

    private fun setIDs() {
        if (id1 % 8 != 0) {
            id11 = 7 - id1 / 8
            if ((id1 / 8) % 2 == 0) {
                id12 = id1 % 8 - 1
            } else if ((id1 / 8) % 2 == 1) {
                id12 = 8 - id1 % 8
            }
        } else {
            id11 = 7 - (id1 / 8 - 1)
            if ((id1 / 8) % 2 == 0) {
                id12 = 0
            } else if ((id1 / 8) % 2 == 1) {
                id12 = 7
            }
        }

        if (id2 % 8 != 0) {
            id21 = 7 - id2 / 8
            if ((id2 / 8) % 2 == 0) {
                id22 = id2 % 8 - 1
            } else if ((id2 / 8) % 2 == 1) {
                id22 = 8 - id2 % 8
            }
        } else {
            id21 = 7 - (id2 / 8 - 1)
            if ((id2 / 8) % 2 == 0) {
                id22 = 0
            } else if ((id2 / 8) % 2 == 1) {
                id22 = 7
            }
        }
    }

    private fun setOnBoard(): Array<Array<String>> {
        var changeRows: Boolean = true
        var counter: Int = 64
        var theBoard = Array(8, { arrayOf(" ", " ", " ", " ", " ", " ", " ", " ") })

        for (i in 0..theBoard.size - 1) {
            var rowArray = arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
            if (changeRows) {
                for (j in 0..7) {
                    rowArray[j] = board[counter]
                    counter--
                    changeRows = false
                }
            } else {
                for (j in 0..7) {
                    rowArray[7 - j] = board[counter]
                    counter--
                    changeRows = true
                }
            }

            theBoard[i] = rowArray
        }

        return theBoard
    }

    fun setPiece() {
        setIDs()
        theBoard = setOnBoard()
        if (board[id1][0] != board[id2][0] && previousPlayer != player) {
            var rookSetter = Executor(Rook())
            var pawnSetter = Executor(Pawn())
            var kingSetter = Executor(King())
            var queenSetter = Executor(Queen())
            var knightSetter = Executor(Knight())
            var bishopSetter = Executor(Bishop())
            checkValidity = rookSetter.executeMovement() || pawnSetter.executeMovement() || kingSetter.executeMovement() || queenSetter.executeMovement() || knightSetter.executeMovement() || bishopSetter.executeMovement()
        }
    }

    inner class Executor(strategy: PieceSetter) {
        private var strategy: PieceSetter = strategy
        fun executeMovement(): Boolean {
            return strategy.move()
        }
    }

    inner class Rook : PieceSetter {

        override fun checkDiagonal(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun checkStraight(): Boolean {
            var tmp = -1
            if (id11 == id21) {
                if (id12 < id22) {
                    tmp = id22 - 1
                    while (tmp != id12) {
                        if (!theBoard[id11][tmp].equals(" ")) {
                            return false
                        }
                        tmp -= 1
                    }
                } else {
                    tmp = id22 + 1
                    while (tmp != id12) {
                        if (!theBoard[id11][tmp].equals(" ")) {
                            return false
                        }
                        tmp += 1
                    }
                }
            } else if (id12 == id22) {
                if (id11 < id21) {
                    tmp = id21 - 1
                    while (tmp != id11) {
                        if (!theBoard[tmp][id12].equals(" ")) {
                            return false
                        }
                        tmp -= 1
                    }
                } else {
                    tmp = id21 + 1
                    while (tmp != id11) {
                        if (!theBoard[tmp][id12].equals(" ")) {
                            return false
                        }
                        tmp += 1
                    }
                }
            }
            return true
        }

        override fun rule(): Int {
            if (id11 == id21 || id12 == id22) {
                if (checkStraight()) {
                    return 1
                }
            }
            return 0
        }

        override fun move(): Boolean {
            if (player == 0) {
                if (board[id1] == "lRook") {
                    if (rule() == 1) {

                        btn1.setImageDrawable(null)
                        btn2.setImageResource(R.drawable.rlt6)
                        return true
                    } else return false

                } else return false
            } else {
                if (board[id1] == "dRook") {
                    if (rule() == 1) {

                        btn1.setImageDrawable(null)
                        btn2.setImageResource(R.drawable.rdt6)
                        return true
                    } else return false

                } else return false
            }

        }
    }

    inner class Pawn : PieceSetter {

        override fun checkDiagonal(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun checkStraight(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun rule(): Int {
            if (player == 0) {
                if ((id11 == 6) && ((id21 == 5 || id21 == 4) && id22 == id12 && theBoard[id21][id22].equals(" "))) {
                    return 1

                } else if ((id11 - 1 == id21 && id12 - 1 == id22) || (id11 - 1 == id21 && id12 + 1 == id22)) {
                    if (theBoard[id21][id22][0] == 'd') {
                        if (id21 == 0) {
                            return 2
                        } else {
                            return 1
                        }
                    }
                } else if ((id21 == id11 - 1) && (id12 == id22) && theBoard[id21][id22].equals(" ")) {
                    if (id21 == 0) {
                        return 2
                    } else {
                        return 1
                    }
                }
                return 0
            } else {
                if ((id11 == 1) && ((id21 == 2 || id21 == 3) && id22 == id12 && theBoard[id21][id22].equals(" "))) {
                    return 1
                } else if ((id11 + 1 == id21 && id12 - 1 == id22) || (id11 + 1 == id21 && id12 + 1 == id22)) {
                    if (theBoard[id21][id22][0] == 'l') {
                        if (id21 == 7) {
                            return 2
                        } else {
                            return 1
                        }
                    }
                } else if ((id21 == id11 + 1) && (id12 == id22) && theBoard[id21][id22].equals(" ")) {
                    if (id21 == 7) {
                        return 2
                    } else {
                        return 1
                    }
                }
                return 0
            }
        }

        override fun move(): Boolean {
            if (player == 0) {
                if (board[id1] == "lPawn") {
                    var result = rule()
                    if (result == 1) {
                        btn1.setImageDrawable(null)
                        btn2.setImageResource(R.drawable.plt6)
                        return true
                    } else if (result == 2) {
                        btn1.setImageDrawable(null)
                        btn2.setImageResource(R.drawable.qlt6)
                        board[id1] = "lQueen"
                        return true
                    } else return false
                } else return false
            } else {
                if (board[id1] == "dPawn") {
                    var result = rule()
                    if (result == 1) {
                        btn1.setImageDrawable(null)
                        btn2.setImageResource(R.drawable.pdt6)
                        return true
                    } else if (result == 2) {
                        btn1.setImageDrawable(null)
                        btn2.setImageResource(R.drawable.qdt6)
                        board[id1] = "dQueen"
                        return true
                    } else return false
                } else return false
            }

        }
    }

    inner class King : PieceSetter {

        override fun checkStraight(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun checkDiagonal(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun rule(): Int {
            if ((id21 == id11 || id21 == id11 - 1 || id21 == id11 + 1) && (id22 == id12 || id22 == id12 - 1 || id22 == id12 + 1)) {
                return 1
            }
            return 0
        }

        override fun move(): Boolean {
            if (player == 0) {
                if (board[id1] == "lKing") {
                    if (rule() == 1) {
                        btn1.setImageDrawable(null)
                        btn2.setImageResource(R.drawable.klt6)
                        return true
                    } /*else if (lKingNeverMoved) {
                        if (id2 == 7 && rlRookNeverMoved && board[6] == " " && board[7] == " ") {
                            btn1.setImageDrawable(null)
                            btn2.setImageResource(R.drawable.klt6)
                            return true
                        } else if (id2 == 3 && llRookNeverMoved && board[2] == " " && board[3] == " " && board[4] == " ") {
                            btn1.setImageDrawable(null)
                            btn2.setImageResource(R.drawable.klt6)
                            return true
                        } else return false
                    }*/ else return false
                } else return false
            } else {
                if (board[id1] == "dKing") {
                    if (rule() == 1) {
                        btn1.setImageDrawable(null)
                        btn2.setImageResource(R.drawable.kdt6)
                        return true
                    } /*else if (dKingNeverMoved) {
                        if (id2 == 58 && rdRookNeverMoved && board[59] == " " && board[58] == " ") {
                            btn1.setImageDrawable(null)
                            btn2.setImageResource(R.drawable.kdt6)
                            return true
                        } else if (id2 == 62 && ldRookNeverMoved && board[61] == " " && board[62] == " " && board[63] == " ") {
                            btn1.setImageDrawable(null)
                            btn2.setImageResource(R.drawable.kdt6)
                            return true
                        } else return false
                    }*/ else return false
                } else return false
            }

        }
    }

    inner class Queen : PieceSetter {

        override fun checkStraight(): Boolean {
            var tmp = -1
            if (id11 == id21) {
                if (id12 < id22) {
                    tmp = id22 - 1
                    while (tmp != id12) {
                        if (!theBoard[id11][tmp].equals(" ")) {
                            return false
                        }
                        tmp -= 1
                    }
                } else {
                    tmp = id22 + 1
                    while (tmp != id12) {
                        if (!theBoard[id11][tmp].equals(" ")) {
                            return false
                        }
                        tmp += 1
                    }
                }
            } else if (id12 == id22) {
                if (id11 < id21) {
                    tmp = id21 - 1
                    while (tmp != id11) {
                        if (!theBoard[tmp][id12].equals(" ")) {
                            return false
                        }
                        tmp -= 1
                    }
                } else {
                    tmp = id21 + 1
                    while (tmp != id11) {
                        if (!theBoard[tmp][id12].equals(" ")) {
                            return false
                        }
                        tmp += 1
                    }
                }
            }
            return true
        }

        override fun checkDiagonal(): Boolean {
            var tmp1 = -1
            var tmp2 = -1
            if (id21 > id11) {
                if (id22 < id12) {
                    tmp1 = id21 - 1
                    tmp2 = id22 + 1
                    while (tmp1 != id11 && tmp2 != id12) {
                        if (!theBoard[tmp1][tmp2].equals(" ")) {
                            return false
                        }
                        tmp1 -= 1
                        tmp2 += 1
                    }
                } else if (id22 > id12) {
                    tmp1 = id21 - 1
                    tmp2 = id22 - 1
                    while (tmp1 != id11 && tmp2 != id12) {
                        if (!theBoard[tmp1][tmp2].equals(" ")) {
                            return false
                        }
                        tmp1 -= 1
                        tmp2 -= 1
                    }
                }
            } else if (id21 < id11) {
                if (id22 < id12) {
                    tmp1 = id21 + 1
                    tmp2 = id22 + 1
                    while (tmp1 != id11 && tmp2 != id12) {
                        if (!theBoard[tmp1][tmp2].equals(" ")) {
                            return false
                        }
                        tmp1 += 1
                        tmp2 += 1
                    }
                } else if (id22 > id12) {
                    tmp1 = id21 + 1
                    tmp2 = id22 - 1
                    while (tmp1 != id11 && tmp2 != id12) {
                        if (!theBoard[tmp1][tmp2].equals(" ")) {
                            return false
                        }
                        tmp1 += 1
                        tmp2 -= 1
                    }
                }
            }
            return true
        }

        override fun rule(): Int {

            if (id11 + id12 == id21 + id22 || id11 - id12 == id21 - id22) {
                if (checkDiagonal()) {
                    return 1
                }
            } else if (id11 == id21 || id12 == id22) {
                if (checkStraight()) {
                    return 1
                }
            }

            return 0

        }

        override fun move(): Boolean {
            if (player == 0) {
                if (board[id1] == "lQueen") {
                    if (rule() == 1) {
                        btn1.setImageDrawable(null)
                        btn2.setImageResource(R.drawable.qlt6)
                        return true
                    } else return false
                } else return false
            } else {
                if (board[id1] == "dQueen") {
                    if (rule() == 1) {

                        btn1.setImageDrawable(null)
                        btn2.setImageResource(R.drawable.qdt6)
                        return true
                    } else return false
                } else return false
            }


        }
    }

    inner class Knight : PieceSetter {

        override fun checkStraight(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun checkDiagonal(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun rule(): Int {
            if (id11 - 2 == id21) {
                if (id12 - 1 == id22 || id12 + 1 == id22) {
                    return 1
                }
            } else if (id11 + 2 == id21) {
                if (id12 - 1 == id22 || id12 + 1 == id22) {
                    return 1
                }
            } else if (id12 - 2 == id22) {
                if (id11 - 1 == id21 || id11 + 1 == id21) {
                    return 1
                }
            } else if (id12 + 2 == id22) {
                if (id11 - 1 == id21 || id11 + 1 == id21) {
                    return 1
                }
            }
            return 0
        }

        override fun move(): Boolean {

            if (player == 0) {
                if (board[id1] == "lKnight") {
                    if (rule() == 1) {
                        btn1.setImageDrawable(null)
                        btn2.setImageResource(R.drawable.nlt6)
                        return true
                    } else return false
                } else return false
            } else {
                if (board[id1] == "dKnight") {
                    if (rule() == 1) {
                        btn1.setImageDrawable(null)
                        btn2.setImageResource(R.drawable.ndt6)
                        return true
                    } else return false
                } else return false
            }

        }

    }

    inner class Bishop : PieceSetter {

        override fun checkStraight(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun checkDiagonal(): Boolean {
            var tmp1 = -1
            var tmp2 = -1
            if (id21 > id11) {
                if (id22 < id12) {
                    tmp1 = id21 - 1
                    tmp2 = id22 + 1
                    while (tmp1 != id11 && tmp2 != id12) {
                        if (!theBoard[tmp1][tmp2].equals(" ")) {
                            return false
                        }
                        tmp1 -= 1
                        tmp2 += 1
                    }
                } else if (id22 > id12) {
                    tmp1 = id21 - 1
                    tmp2 = id22 - 1
                    while (tmp1 != id11 && tmp2 != id12) {
                        if (!theBoard[tmp1][tmp2].equals(" ")) {
                            return false
                        }
                        tmp1 -= 1
                        tmp2 -= 1
                    }
                }
            } else if (id21 < id11) {
                if (id22 < id12) {
                    tmp1 = id21 + 1
                    tmp2 = id22 + 1
                    while (tmp1 != id11 && tmp2 != id12) {
                        if (!theBoard[tmp1][tmp2].equals(" ")) {
                            return false
                        }
                        tmp1 += 1
                        tmp2 += 1
                    }
                } else if (id22 > id12) {
                    tmp1 = id21 + 1
                    tmp2 = id22 - 1
                    while (tmp1 != id11 && tmp2 != id12) {
                        if (!theBoard[tmp1][tmp2].equals(" ")) {
                            return false
                        }
                        tmp1 += 1
                        tmp2 -= 1
                    }
                }
            }
            return true
        }

        override fun rule(): Int {
            if (id11 + id12 == id21 + id22 || id11 - id12 == id21 - id22) {
                if (checkDiagonal()) {
                    return 1
                }
            }

            return 0
        }

        override fun move(): Boolean {
            if (player == 0) {
                if (board[id1] == "lBishop") {
                    if (rule() == 1) {
                        btn1.setImageDrawable(null)
                        btn2.setImageResource(R.drawable.blt6)
                        return true
                    } else return false
                } else return false
            } else {
                if (board[id1] == "dBishop") {
                    if (rule() == 1) {
                        btn1.setImageDrawable(null)
                        btn2.setImageResource(R.drawable.bdt6)
                        return true
                    } else return false
                } else return false
            }

        }
    }

}