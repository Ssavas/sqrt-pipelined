/*
	Suleyman Savas
	2017-09-04
	Halmstad University

	Main processing part of the square root method.
	
*/

package SquareRoot

import chisel3._
import chisel3.util._

class Process extends Module{
	val io = IO(new Bundle{
		val X_in = Input(Bits(width = 26.W))
		val Y_out = Output(Bits(width = 23.W))
	})

	// instantiate lookup tables
	val tableC = Module(new lookupC())
	val tableL = Module(new lookupL())
	val tableJ = Module(new lookupJ())

	val x_w                   = Wire(Bits(20.W))
	val s1                    = Wire(Bits(27.W))
	val jx                    = Wire(Bits(42.W))
	val jx_adjusted           = Wire(Bits(22.W))
	val x_w2                  = Wire(Bits(40.W))
	val x_w2_adjusted         = Wire(Bits(27.W))
	val cx_w2                 = Wire(Bits(43.W))
	val cx_w2_adjusted        = Wire(Bits(16.W))
	val l_minus_jx            = Wire(Bits(28.W))
	val l_minus_jx_plus_cx_w2 = Wire(Bits(28.W))
	val s1s2                  = Wire(Bits(55.W))

	// Registers for the first pipeline stage
	val tableLReg1  = RegNext(tableL.io.out)	
	val tableJReg1  = RegNext(tableJ.io.out)
	val tableCReg1  = RegNext(tableC.io.out)
	val squarerReg1 = RegNext(x_w2_adjusted)
	val x_wReg1     = RegNext(x_w)
	val s1Reg1      = RegNext(s1)

	// Registers for the second pipeline stage
	val jx_adjustedReg1    = RegNext(jx_adjusted)
	val cx_w2_adjustedReg1 = RegNext(cx_w2_adjusted)
	val tableLReg2         = RegNext(tableLReg1)
	val s1Reg2             = RegNext(s1Reg1)

	// Registers for the third pipeline stage
	val l_minus_jx_plus_cx_w2Reg1 = RegNext(l_minus_jx_plus_cx_w2)
	val s1Reg3                    = RegNext(s1Reg2)

	val coeffAddr   = io.X_in(25, 20)
	tableC.io.addr := coeffAddr
	tableL.io.addr := coeffAddr
	tableJ.io.addr := coeffAddr

	val tmp = Wire(Bits(7.W))
	x_w := io.X_in(19, 0)	//x_20
	s1  := Cat(io.X_in, "b0".U)

	x_w2          := x_w * x_w
	x_w2_adjusted := x_w2(39, 13)

	jx          := tableJReg1 * x_wReg1
	jx_adjusted := jx(41, 20)

	cx_w2          := tableCReg1 * squarerReg1
	cx_w2_adjusted := cx_w2(42, 27)

	l_minus_jx            := tableLReg2 - jx_adjustedReg1
	l_minus_jx_plus_cx_w2 := l_minus_jx + cx_w2_adjustedReg1

	s1s2 := s1Reg3(26, 1) * l_minus_jx_plus_cx_w2Reg1

	io.Y_out := s1s2(52, 30)
	
//printf("process in: %d out: %d\n", io.X_in, io.Y_out)

//printf("L reg: %d J reg: %d C reg: %d squarer reg: %d x_w reg: %d s1 reg: %d \n", tableLReg1, tableJReg1, tableCReg1, squarerReg1, x_wReg1, s1Reg1)
//printf("L    : %d J    : %d C    : %d squarer    :%d x_w     : %d s1    : %d \n", tableL.io.out, tableJ.io.out, tableC.io.out, x_w2_adjusted, x_w, s1)
}
