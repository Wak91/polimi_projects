!#/bin/bash

echo "------- CENTRALIZED FLAT TABLE OSX INIT SCRIPT ------- \n"

echo "spawn the group controller..."

osascript -e 'tell application "Terminal" to do script "java -jar $HOME/Desktop/SetupGroupController.jar"'

echo "spawn the groups members..."

for member in $(seq 1 8); do
	
	ID=${member};
	PORT=${member}${member}${member}${member};
	COMMAND="java -jar $HOME/Desktop/SetupGroupMember.jar $(echo $ID) $(echo $PORT) 1";
	echo $COMMAND;
	
	osascript -e 'tell application "Terminal" to do script "'"$COMMAND"'"'

done

echo "\nSetup finished!"
