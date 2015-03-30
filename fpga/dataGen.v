module dataGen #( parameter NUM_LEDS = 60 )(input clk, input reset, input run, output reg [(NUM_LEDS*24)-1:0] data);

  always @(posedge clk) begin
    
    if (reset) begin
      //First channel of first led on, rest off
      data <= {8'hFF, {(NUM_LEDS*24-8){1'b0} }};
    end else begin
       if (run) begin
          data <= {data[7:0], data[(NUM_LEDS*24)-1:8]};
       end
    end
    
    
  end

endmodule