#!/bin/bash

# Step 1: Run "mvn clean install"
mvn clean install

# Check if "mvn clean install" was successful before proceeding
if [ $? -eq 0 ]; then
  echo "Maven build successful"
else
  echo "Maven build failed. Exiting..."
  exit 1
fi

# Step 2: Run "docker build -t spring_boot_service ."
docker build -t spring_boot_service .

# Check if "docker build" was successful before proceeding
if [ $? -eq 0 ]; then
  echo "Docker build successful"
else
  echo "Docker build failed. Exiting..."
  exit 1
fi

# Step 3: Save the Docker image to a local tar file
docker save -o spring_boot.tar spring_boot_service

# Check if "docker save" was successful before proceeding
if [ $? -eq 0 ]; then
  echo "Docker image saved to spring_boot.tar"
else
  echo "Docker save failed. Exiting..."
  exit 1
fi

# Step 4: Copy the tar file to the remote server using scp
tar_file="spring_boot.tar"
remote_server="ubuntu@130.225.39.184"
ssh_key_path="~/.ssh/aaustrato"

scp -i $ssh_key_path $tar_file $remote_server:~

# Check if scp was successful
if [ $? -eq 0 ]; then
  echo "File copied to remote server successfully"
  # Remove the local tar file after it has been sent
  rm $tar_file
  echo "Local tar file removed"
else
  echo "File copy to remote server failed. Exiting..."
  exit 1
fi

echo "All tasks completed successfully"
