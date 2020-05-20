for audioSrcName in ./*.wav; do
	echo "do $audioSrcName? (y/n)"
	read doFile
	if [ $doFile == "y" ]; then
		audioOneName=${audioSrcName%.*}
		audioName=${audioOneName:2}
		echo "What is the tempo of $audioName?"
		read tempo
		outFileName="../Songs/$audioName AubioMix.dat"
		echo "$outFileName"
		echo "$tempo," > "$outFileName"
		echo "./src/danceDanceRevolution/assets/audioFiles/${audioSrcName:2}" >> "$outFileName"
		aubio onset "$audioSrcName" >> "$outFileName"
		echo "," >> "$outFileName"
		aubio pitch "$audioSrcName" >> "$outFileName"		
	fi


done
