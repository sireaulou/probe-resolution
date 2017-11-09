clear;

sx = [1.83,-1.83,1.76,-1.76,1.58,-1.58,-2.25];
sy = -[-2.87,-2.87,-2.95,-2.95,2.78,2.78,2.84];

dx = [.89,-.89,.73,-.73];
dy = -[-.36,-.36,.18,.18];

plot(sx,sy,'.r','MarkerSize',15);
hold on 
plot(dx,dy,'.b','MarkerSize',15);
plot(-100,100,'k');
rectangle('Position',[-1 -1 2 2]);
hold off
daspect([1 1 1]);
axis([-5, 5, -3, 3]);
legend('Source','Detector','Phantom');

% Fittest 437
% x: [1.83,-1.83,1.76,-1.76,1.58,-1.58,-2.25,.89,-.89,.73,-.73,];
% y: [-2.87,-2.87,-2.95,-2.95,2.78,2.78,2.84,-.36,-.36,.18,.18,];
% 19.94