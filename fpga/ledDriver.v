module ledDriver #( parameter NUM_LEDS = 60 )(input clk, input reset, output reg led, input[(NUM_LEDS*24)-1:0] inData, input start, output finish);

	//Generate the pulse width modulated output signal
	
	//Running at 20MHz, a High pulse of 8 cycles and 17 low cycles produces a 0,
	//16 cycles of high and 9 low a 1. 
	
	//Are we doing stuff right now?
	reg running = 0;
	
	//Are we waiting for RES?
	reg resCounting = 0;
	
	
	//The data we operate on
  reg[(NUM_LEDS*24)-1:0] data;
	
	function integer clog2;
  input integer value;
  begin
    value = value-1;
    for (clog2=0; value>0; clog2=clog2+1)
      value = value>>1;
    end
  endfunction

	
	parameter LOG_BITS = clog2(NUM_LEDS*24);
	
	reg[LOG_BITS-1:0] bitCnt = 0;
	
	
	
	//Count repeatedly counts from 0 to 24.
	reg[4:0] count = 0;
	always @(posedge clk) begin
		if (reset || ~running) begin
			count<=0;
		end else begin
			if (count!=24) begin
				count <= count + 1;
			end else begin
			 count <=0;
			end
		end
	end
	
	wire lastBit;
  assign lastBit = (bitCnt==(NUM_LEDS*24-1));
  
  reg firstbit = 0;
	
	always @(posedge clk) begin
	  if (reset) begin
	    running <= 0;
	    firstbit <= 1;
	  end else begin
	    if (~running & start) begin
	    
	      running <= 1;
	      data <= inData;
	      bitCnt<= 0;
	      firstbit <= 1;
	    
	    end else if (running) begin
	      firstbit<=0;
	      if (lastBit && count==24) begin
	        running <= 0;
	        resCounting <= 1;
	      end else if (count==0 && ~firstbit) begin
	        data <= data << 1;
	        bitCnt <= bitCnt +1;
	        
	      end
	      
	    end else if (resCounting && finish) begin
	      resCounting <= 0;
	    end
	  end
	end

	
	
	//The bit being currently output
	wire current;
	//Is always the highest bit in the shift reg
	assign current = data[(NUM_LEDS*24)-1];
	
	//Pulse out the data
	always @(posedge clk) begin
		if (reset || ~running) begin
			led <= 0;
		end else begin
			if (count==0) begin
				led <= 1;
			end else if ( (count==8 && ~current) || (count==16 && current)) begin
				led<=0;
			end
		end
	end
		
	//Counter for res contition
	reg [9:0] resCounter=0;
	always @(posedge clk) begin
	  
	  if (reset || ~resCounting) begin
	    resCounter=0;
	  end else begin
	    
	    resCounter <= resCounter+1;
	    	    	    
	  end
	  
	end
	
	assign finish = resCounter==10'h3FF;
	
endmodule