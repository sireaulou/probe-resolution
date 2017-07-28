%Reproduces Fig.3a from Culver's paper.
%Refer to my doc.

clear;
addpath('../functions');

%case 1: S0 = 0.05, scaled
%case 2: S0 = 0.5 scaled
%case 3: S0 = 0.5 fixed

L = 60;
% dx = [3 4.5 6 9 10 11.25 15 18 22.5] mm
%fixed FOV;
M = ([31 21 16 11 10 9 7 6 5]).^2; 
%unfixed FOV;
% M = 900*ones(9,1);
N = 900;
S01 = 0.05;
S02 = 0.5;
S03 = 0.5;
vol = 6*6*0.2;

for i=1:9
   alpha1 = sqrt(M(i))*(0.0003)/(S01/vol);
   alpha2 = sqrt(M(i))*(0.0003)/(S02/vol);
   alpha3 = sqrt(1/M(i))*(0.001*sqrt(M(i)))/(S03/vol);
   s{i} = svdLoad(strcat('../../culverReproduction/culver3aUnfixed/culver3aUnfixed_',num2str(i)));
   diff1 = abs(s{i}-alpha1);
   diff2 = abs(s{i}-alpha2);
   diff3 = abs(s{i}-alpha3);
   [m1,ind1] = min(diff1);
   [m2,ind2] = min(diff2);
   [m3,ind3] = min(diff3);
   res1(i)=2*L/ind1^(1/2);
   res2(i)=2*L/ind2^(1/2);
   res3(i)=2*L/ind3^(1/2);
end
dxs = [3 4.5 6 9 10 11.25 15 18 22.5];
semilogx(dxs,res1,'.-');
hold on;
semilogx(dxs,res2,'-*');
semilogx(dxs,res3,'-d');
hold off;
xlim([2,23]);
xlabel('dx optode (mm)');
ylabel('<res> (mm)');
legend('Sig = 0.05, scaled','Sig = 0.5, scaled', 'Sig = 0.5, fixed');