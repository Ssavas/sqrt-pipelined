@Author: Suleyman Savas - Halmstad University, 2017-08-22

Square Root implementation based on the Harmonized Parabolic Synthesis method 
by Erik hertz.

How to compile:

- Go into the scala folder
- To run the tests and generate harness:
sbt "run --backend c --compile --test --genHarness"

- Generating verilog:
sbt "run --backend v --genHarness"

Details: https://chisel.eecs.berkeley.edu/2.2.0/getting-started.html
