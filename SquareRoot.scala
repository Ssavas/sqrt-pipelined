/*
	Suleyman Savas
	2017-08-22
	Halmstad University
*/

package SquareRoot

import chisel3._
import chisel3.util._
import chisel3.iotesters.{PeekPokeTester, Driver, ChiselFlatSpec}

class sqrt() extends Module {
	val io = IO(new Bundle{
		val en     = Input(Bool())
		val input  = Input(Bits(32.W))
		val output = Output(Bits(32.W))
		val valid  = Output(Bool())
	})

	val preProcessor  = Module(new PreProcess())
	val processor     = Module(new Process())
	val postProcessor = Module(new PostProcess())

	// The pipeline registers after the pre-processor
	val preOut1Reg1 = Reg(next = preProcessor.io.out1)	//mantissa
	val preOut2Reg1 = Reg(next = preProcessor.io.out2)	//exponent
	val preOut1Reg2 = Reg(next = preOut1Reg1)	
	val preOut2Reg2 = Reg(next = preOut2Reg1)	
	val inputReg1   = RegNext(io.input(30,23))	// exponent
	val inputReg2   = RegNext(inputReg1)
	val signReg1    = RegNext(io.input(31))
	val signReg2    = RegNext(signReg1)
	val enReg1      = Reg(init = 0.U, next = io.en)
	val enReg2      = Reg(init = 0.U, next = enReg1)


	//The pipeline registers for stage 2
	val enReg3      = Reg(init = 0.U, next = enReg2)
	val inputReg3   = RegNext(inputReg2)
	val preOut2Reg3 = Reg(next = preOut2Reg2)
	val signReg3    = RegNext(signReg2)

	// The pipeline registers for stage 3
	val enReg4      = Reg(init = 0.U, next = enReg3)
	val inputReg4   = RegNext(inputReg3)
	val preOut2Reg4 = RegNext(preOut2Reg3)
	val signReg4    = RegNext(signReg3)

	// The pipeline registers for stage 4
	val enReg5      = Reg(init = 0.U, next = enReg4)
	val inputReg5   = RegNext(inputReg4)
	val preOut2Reg5 = RegNext(preOut2Reg4)
	val signReg5    = RegNext(signReg4)


	preProcessor.io.in       := io.input
	processor.io.X_in        := preOut1Reg2
	postProcessor.io.Y_in    := processor.io.Y_out
	postProcessor.io.exp_in  := preOut2Reg5

	when(inputReg5 === 0.U){	//when the input is 0, preprocessing and postprocessing screws
		io.output := 0.U
	}
	.elsewhen(inputReg5 === 255.U){
		io.output := Cat(signReg5, inputReg5, processor.io.Y_out)
	}
	.otherwise{
		io.output := postProcessor.io.Z_out
	}

	io.valid := enReg5
	printf("inputReg1: %d input exp: %d\n", inputReg2, io.input(30,23))
//	printf("input: %d output: %d valid: %d\n", io.input, io.output, io.valid)

}

class SqrtTest(c: sqrt) extends PeekPokeTester(c) {

/*
	In matlab I use typecast(single(4), 'uint32') to get the ieee single precision format value with the representation in uint format.
*/



	poke(c.io.input, 2139095040.U);	// +Inf
	poke(c.io.en, 1.U);
	//expect(c.io.output, INF);	// +Inf
	step(1);

	poke(c.io.input, "b11111111100000000000000000000000".U);	// -Inf
	poke(c.io.en, 1.U);
	//expect(c.io.output, INF);	// -Inf
	step(1);

	poke(c.io.input, 1082130432.U);	// 4.0
	poke(c.io.en, 1.U);
	//expect(c.io.output, 1073741824.U);	//2.0
	step(1);

	poke(c.io.input, 1130561536.U);	// 227
	poke(c.io.en, 1.U);
	//expect(c.io.output, 1097928822.U); //15.066519173319364
	step(1);

	poke(c.io.input, 0.U);	// 227
	poke(c.io.en, 1.U);
	//expect(c.io.output, 1097928822.U); //15.066519173319364
	step(1);

	poke(c.io.input, 1018070301.U);	// 0.0213037
	poke(c.io.en, 1.U);
	//expect(c.io.output, 0.U); //15.066519173319364
	step(1);

	poke(c.io.input, 1028070301.U);	//
	poke(c.io.en, 1.U);
	//expect(c.io.output, 0.U); //
	step(1);

	poke(c.io.input, 1038070301.U);	//	0.1092264
	poke(c.io.en, 1.U);
	step(1);

	poke(c.io.input, 1190797887.U);	//	32017.123
	poke(c.io.en, 1.U);
	step(1);

	poke(c.io.input, 1259869139.U);	//	9966547.222
	poke(c.io.en, 1.U);
	step(1);

	poke(c.io.input, 1089051034.U);	//	7.3
	poke(c.io.en, 1.U);
	step(1);

	poke(c.io.en, 0.U);
	step(1);
	step(1);
	step(1);
	step(1);
	step(1);
}

object sqrt {
  def main(args: Array[String]): Unit = {
    if (!Driver(() => new sqrt())(c => new SqrtTest(c))) System.exit(1)
  }
}

