onerror { resume }
transcript off
add wave -noreg -logic {/test_top/uut/spiClk}
add wave -noreg -logic {/test_top/uut/ledClk}
add wave -noreg -logic {/test_top/uut/spiOut}
add wave -noreg -logic {/test_top/uut/spiIn}
add wave -noreg -logic {/test_top/uut/led}
add wave -noreg -hexadecimal -literal -unsigned {/test_top/uut/writeCount}
add wave -noreg -logic {/test_top/uut/write}
add wave -noreg -logic {/test_top/uut/reset}
add wave -noreg -logic {/test_top/uut/readData}
add wave -noreg -logic {/test_top/uut/read}
add wave -noreg -logic {/test_top/uut/q2}
add wave -noreg -logic {/test_top/uut/q1}
add wave -noreg -logic {/test_top/uut/ledFinished}
add wave -noreg -logic {/test_top/uut/justSwitched}
add wave -noreg -logic {/test_top/uut/switch}
add wave -noreg -logic {/test_top/uut/seenSwitch}
add wave -noreg -logic {/test_top/uut/spiFinished}
add wave -noreg -logic {/test_top/uut/spiRealFinished}
add wave -noreg -logic {/test_top/uut/firstRun}
add wave -noreg -logic {/test_top/uut/empty2}
add wave -noreg -logic {/test_top/uut/empty1}
add wave -noreg -logic {/test_top/uut/empty}
add wave -noreg -logic {/test_top/uut/ledDriver_inst/start}
add wave -noreg -logic {/test_top/mask}
add wave -noreg -logic {/test_top/uut/canSwitch}
cursor "Cursor 1" 152883909ps  
cursor "Cursor 2" 152210578ps  
transcript on
