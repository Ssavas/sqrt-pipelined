/*
	Suleyman Savas
	2017-09-04
	Halmstad University

	Converting base 2 to base 4 and representing it in binary
*/

package SquareRoot

import chisel3._
import chisel3.util._

class PreProcess() extends Module{
	val io = IO(new Bundle {
		val in   = Input(Bits(width = 32.W))		// x_32
		val out1 = Output(Bits(width = 26.W))		// x_26
		val out2 = Output(SInt(width = 8.W))		//exp_out
//		val out3 = Output(Bits(width = 1.W))
	})

	val oneThird          = "b001010101010101010101010".U
	val subOneFrom_m_odd  = "b010000000000000000000000".U
	val subOneFrom_m_even = "b100000000000000000000000".U

	val m           = Wire(Bits(width = 24.W))
	val exp         = Wire(SInt(width = 8.W))
	val expAfterDiv = Wire(SInt(width = 8.W))
	val mAfterDiv   = Wire(Bits(width = 48.W))
	val mAfterSub   = Wire(Bits(width = 24.W))
	val expAfterSub = Wire(SInt(width = 8.W))


	exp         := io.in(30, 23).toSInt
	expAfterSub := exp - 127.S
	m           := Cat(1.U, io.in(22, 0))

/*	when(exp === 11111111.S){
		io.out1 := 0.U
		io.out2 := 11111111.S
		io.out3 := io.in(31)
	}
	.elsewhen(expAfterSub(0) === 1.U){*/
	when(expAfterSub(0) === 1.U){
		mAfterSub   := m - subOneFrom_m_odd
		mAfterDiv   := mAfterSub * oneThird
		io.out1     := mAfterDiv(44, 19)
		expAfterDiv := (expAfterSub - 1.S) >> 1
		io.out2     := expAfterDiv
	}
	.otherwise{
		mAfterSub   := m - subOneFrom_m_even
		//mAfterSub   := Cat(0.U, m(22,0))
		mAfterDiv   := mAfterSub * oneThird
		io.out1     := mAfterDiv(45, 20)
		expAfterDiv := expAfterSub >> 1
		io.out2     := expAfterDiv
	}

//printf("exp: %d expAfterDiv: %d expAfterSub: %d \n", exp, expAfterDiv, expAfterSub)

}
