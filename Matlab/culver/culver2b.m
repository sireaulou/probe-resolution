%Reproduces Fig2b from Culver's paper
%Refer to my doc.

clear;
addpath('../functions');

svd8x8 = svdLoad('../../culverReproduction/tenthCulver/tenthCulver2_21mm_8x8');
svd80x80 = svdLoad('../../culverReproduction/tenthCulver/tenthCulver2_21mm_80x80');
svd30x30 = svdLoad('../../culverReproduction/tenthCulver/tenthCulver2_87mm_30x30');

semilogy(svd8x8)
hold on
semilogy(svd80x80);
semilogy(svd30x30);
hold off
ylim([1e-5 10]);
xlim([1 18]);
legend('FOV = 21 mm; SD = 8 x 8','FOV = 21 mm; SD = 80 x 80','FOV = 87 mm; SD = 30 x 30');
