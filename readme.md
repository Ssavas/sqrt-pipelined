@Author: Suleyman Savas - Halmstad University, 2017-08-22 contact: suleyman_savas@hotmail.com

Square Root implementation based on the Harmonized Parabolic Synthesis method by Erik Hertz.

The hardware description language that is used for this implementation is Chisel. The implementation is with 5 pipeline stages. Therefore, it produces a result in 5 cycles. If fed continuously, the throughput is 1 result/cycle. There is a non-pipelined version under my git account.

The implementation is synthesized on two different FPGAs. The results and further dicussions can be found in my article titled "Using Harmonized Parabolic Synthesis to Implement a Single-Precision Floating-Point SquareRoot Unit" (http://urn.kb.se/resolve?urn=urn:nbn:se:hh:diva-39322). Please refer to it if you use this implementation.

How to compile:

Go into the folder

To run the tests and generate harness: sbt "run --backend c --compile --test --genHarness"

Generating verilog: sbt "run --backend v --genHarness"

Details: https://chisel.eecs.berkeley.edu/2.2.0/getting-started.html
