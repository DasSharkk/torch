cd ~ || exit
rm -rf torch-installation
git clone https://github.com/mooziii/torch.git torch-installation
cd torch-installation || exit
chmod +x gradlew
./gradlew installDist
echo "hi"
cp -r torch/build/install/torch install/torch
clear
cd install || exit
echo "###################################################################"
echo "# You'll be prompted for your password in order to install torch. #"
echo "###################################################################"
chmod +x copy.sh
sudo ./copy.sh
rm -rf torch
cd ..
rm -rf torch-installation
clear
echo "torch was installed successfully"