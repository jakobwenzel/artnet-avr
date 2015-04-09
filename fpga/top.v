module top(input spiClk, input spiIn, output spiOut, output led);

	parameter NUM_LEDS = 170;
					
	wire ledClk;   
	reg reset = 1;
	always @(posedge ledClk) begin
		reset <= 0;
	end
	
	reg switch = 0;
					 
	
	wire q1,q2;
	
	reg[12:0] writeCount=0;
	
	wire read;	
	wire write ;		   	   
	
	wire empty1, empty2;
	reg justSwitched = 0;
	fifo fifo1(
		.Data(spiIn),
		.WrClock(spiClk),
		.RdClock(ledClk),
		.WrEn(~switch & write),
		.RdEn(read & switch),
		.Reset(reset),
		.RPReset(1'b0),
		.Q(q1),
		.Empty(empty1),
		.Full(),
		.AlmostEmpty(),
		.AlmostFull()
	);	   
	
	fifo fifo2(
		.Data(spiIn),
		.WrClock(spiClk),
		.RdClock(ledClk),
		.WrEn(switch & write),
		.RdEn(read & ~switch),
		.Reset(reset),
		.RPReset(1'b0),
		.Q(q2),
		.Empty(empty2),
		.Full(),
		.AlmostEmpty(),
		.AlmostFull()
	);		  
	wire readData;
	assign readData = switch ? q1 : q2;
	wire empty;
	assign empty = switch ? empty1 : empty2;
	
	wire ledFinished;  
	
	wire readDriver;
	assign read = readDriver | justSwitched;
	
	ledDriver ledDriver_inst(
		.clk(ledClk),
		.reset(reset),
		.led(led),
		.data(readData),   
		.lastBit(empty),
		.start(justSwitched),
		.finish(ledFinished),
		.read(readDriver)				  
	);
	
	
	reg ledRealFinished = 0;
										 
	reg firstRun = 1;
	always @(posedge ledClk or posedge reset) begin
		if (reset) begin
			firstRun <= 1;		 
			ledRealFinished <= 1;
		end else begin
			if (justSwitched) begin
				firstRun <= 0;				
			end
			if (ledFinished)
				ledRealFinished <= 1;
			else if (justSwitched)
				ledRealFinished <= 0;
		end
	end
	
	reg spiFinished = 0;
	assign write = ~spiFinished | (seenSwitch!=switch);	
	
	wire spiRealFinished;
	assign spiRealFinished = spiFinished & (seenSwitch==switch);
	
	reg seenSwitch = 0;
	always @(posedge spiClk or posedge reset) begin
		if (reset) begin 
			writeCount <= 0;
			//write <= 0;			
			spiFinished <= 0;
			seenSwitch <= 0;
		end else begin
			if (writeCount==NUM_LEDS*24-1) begin   
				if (seenSwitch!=switch) begin
					writeCount <= 1;  
					spiFinished <= 0;
				end else begin
					spiFinished <= 1;
				end
			end else begin
				writeCount <= writeCount + 1;
				spiFinished <= 0;		 
				seenSwitch <= switch;
			end
		end
	end			 
	
	reg canSwitch = 1;
	
	always @(posedge ledClk or posedge reset) begin
		if (reset) begin
			switch <= 0;			  
			justSwitched <= 0;
			canSwitch <= 1;
		end else begin	
			if (~spiFinished)
				canSwitch <= 1;
			//if ((firstRun | ledFinished) & spiFinished & (seenSwitch==switch)) begin
			if ((firstRun | ledRealFinished) & spiRealFinished) begin
				switch <= ~switch;
				justSwitched <= 1;
				canSwitch <= 0;
			end else
				justSwitched <= 0;
		end
	end	
	
	
	/*
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
	*/

	
	// Internal Oscillator
	// defparam OSCH_inst.NOM_FREQ = "2.08";// This is the default frequency
	defparam OSCH_inst.NOM_FREQ = "20.46";
	OSCH OSCH_inst( .STDBY(1'b0), // 0=Enabled, 1=Disabled
		// also Disabled with Bandgap=OFF
		.OSC(ledClk),
		.SEDSTDBY()
	); // this signal is not required if not
	// using SED

endmodule