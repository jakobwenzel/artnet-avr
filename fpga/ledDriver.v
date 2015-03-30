module ledDriver #( parameter NUM_LEDS = 60 )(input clk, input reset, output reg led, input[(NUM_LEDS*24)-1:0] inData, input start, output finish);

	//Generate the pulse width modulated output signal
	
	//Running at 20MHz, a High pulse of 8 cycles and 17 low cycles produces a 0,
	//16 cycles of high and 9 low a 1. 

	
	//Count repeatedly counts from 0 to 24.
	reg[4:0] count = 0;
	always @(posedge clk) begin
		if (reset) begin
			count<=0;
		end else begin
			if (|count) begin
				count <= count + 1;
			end else count <=0;
		end
	end
	
	reg[(NUM_LEDS*24)-1:0] data;
	
	//The bit being currently output
	wire current;
	//Is always the highest bit in the shift reg
	assign current = data[(NUM_LEDS*24)-1];
	
	//Pulse out the data
	always @(posedge clk) begin
		if (reset) begin
			led <= 0;
		end else begin
			if (count==0) begin
				led <= 1;
			end else if ( (count==8 && ~current) || (count==16 && current)) begin
				led<=0;
			end
		end
	end
endmodule