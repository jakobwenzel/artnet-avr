onerror {resume}
quietly WaveActivateNextPane {} 0
add wave -noupdate /test_ledDriver/clk
add wave -noupdate /test_ledDriver/uut/inData
add wave -noupdate /test_ledDriver/uut/data
add wave -noupdate /test_ledDriver/uut/bitCnt
add wave -noupdate /test_ledDriver/finish
add wave -noupdate /test_ledDriver/led
add wave -noupdate /test_ledDriver/reset
add wave -noupdate /test_ledDriver/start
add wave -noupdate /test_ledDriver/uut/current
add wave -noupdate /test_ledDriver/uut/lastBit
add wave -noupdate /test_ledDriver/uut/count
TreeUpdate [SetDefaultTree]
WaveRestoreCursors {{Cursor 1} {339882613 ps} 0} {{Cursor 2} {2775000 ps} 0}
quietly wave cursor active 1
configure wave -namecolwidth 150
configure wave -valuecolwidth 100
configure wave -justifyvalue left
configure wave -signalnamewidth 1
configure wave -snapdistance 10
configure wave -datasetprefix 0
configure wave -rowmargin 4
configure wave -childrowmargin 2
configure wave -gridoffset 0
configure wave -gridperiod 1
configure wave -griddelta 40
configure wave -timeline 0
configure wave -timelineunits ns
update
WaveRestoreZoom {331212500 ps} {396837500 ps}
