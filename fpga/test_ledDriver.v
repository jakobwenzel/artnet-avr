`timescale 1ns/1ps
module test_ledDriver();

	parameter NUM_LEDS = 1;
	reg clk=0;
	reg reset=1;
	reg[(NUM_LEDS*24)-1:0] data=0;
	reg start=0;
	
	wire led;
	wire finish;
	
	
	ledDriver #( .NUM_LEDS(NUM_LEDS) ) uut (
		.clk(clk), .reset(reset), .led(led), .inData(data), .start(start), .finish(finish)
	);
	
	initial begin
	
		#200
		reset = 0;
		
	end


	//20 MHz clock
	always begin
		#50
		clk = ~clk;
	end

endmodule