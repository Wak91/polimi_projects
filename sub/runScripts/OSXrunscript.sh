!#/bin/bash

echo "------- CENTRALIZED FLAT TABLE OSX INIT SCRIPT ------- \n"

echo "spawn the group controller..."

osascript -e 'tell application "Terminal" to do script "java -jar $HOME/Desktop/SetupGroupController.jar"'

echo "spawn the groups members..."

for member in $(seq 0 2); do

	osascript -e 'tell application "Terminal" to do script "java -jar $HOME/Desktop/SetupGroupMember.jar"'

done

echo "\nSetup finished!"
