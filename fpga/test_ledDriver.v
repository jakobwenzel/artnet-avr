`timescale 1ns/1ps
module test_ledDriver();

	parameter NUM_LEDS = 10;
	reg clk=0;
	reg reset=1;
	wire[(NUM_LEDS*24)-1:0] data;
	reg start=1;
	
	wire led;
	wire finish;
	
	always @(posedge clk) begin
	  if (reset) begin
	    start <= 1;
	  end else begin
	    start <= finish;
	  end
	end
	
	ledDriver #( .NUM_LEDS(NUM_LEDS) ) uut (
		.clk(clk), .reset(reset), .led(led), .inData(data), .start(start), .finish(finish)
	);
	
	dataGen #(.NUM_LEDS(NUM_LEDS) ) dataGen_inst (
	  .clk(clk), .reset(reset), .run(finish), .data(data)
	);
	
	initial begin
	
		#200
		reset = 0;
		
	end


	//20 MHz clock
	always begin
		#25
		clk = ~clk;
	end

endmodule