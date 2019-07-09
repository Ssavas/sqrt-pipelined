/*
	Suleyman Savas
	2017-09-04
	Halmstad University

	Post processing part of the square root method.
	
*/

package SquareRoot

import chisel3._
import chisel3.util._

class PostProcess() extends Module{
	val io = IO(new Bundle {
		val Y_in    = Input(Bits(23.W))
		val exp_in  = Input(SInt(8.W))
		val Z_out   = Output(Bits(32.W))
	})

	io.Z_out := Cat("b0".U, io.exp_in + 127.S , io.Y_in)

}
