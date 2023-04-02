echo "#!/usr/bin/bash" > /usr/bin/torch
echo "/usr/share/torch/torch/bin/torch \"\$@\"" >> /usr/bin/torch
chmod +x /usr/bin/torch
mkdir /usr/share/torch/
cp -r torch /usr/share/torch/
chmod +x /usr/share/torchtorch/bin/torch