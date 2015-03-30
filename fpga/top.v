module top(input spiClk, input spiIn, output spiOut, output led);

	parameter NUM_LEDS = 20;
	wire clk;
	wire reset;
	wire[(NUM_LEDS*24)-1:0] data;
	reg start=1;
	
	wire finish;
	
	always @(posedge clk or posedge reset) begin
	  if (reset) begin
	    start <= 1;
	  end else begin
	    start <= finish;
	  end
	end
	
	reg temp;
	always @(posedge clk) begin  
		temp <= start;
	end
	
	
	ledDriver #( .NUM_LEDS(NUM_LEDS) ) uut (
		.clk(clk), .reset(reset), .led(led), .inData(data), .start(temp), .finish(finish)
	);
	
	dataGen #(.NUM_LEDS(NUM_LEDS) ) dataGen_inst (
	  .clk(clk), .reset(reset), .run(finish), .data(data)
	);
	
	
	GSR GSR_INST (.GSR (reset));

	
	// Internal Oscillator
	// defparam OSCH_inst.NOM_FREQ = "2.08";// This is the default frequency
	defparam OSCH_inst.NOM_FREQ = "20.46";
	OSCH OSCH_inst( .STDBY(1'b0), // 0=Enabled, 1=Disabled
		// also Disabled with Bandgap=OFF
		.OSC(clk),
		.SEDSTDBY()
	); // this signal is not required if not
	// using SED

endmodule