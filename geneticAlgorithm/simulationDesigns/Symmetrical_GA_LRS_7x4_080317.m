clear;

sx = [-.01,.01,.02,-.02,.0,-.0,.0];
sy = -[-.22,-.22,-.15,-.15,-.13,-.13,-.08];

dx = [3.09,-3.09,2.45,-2.45];
dy = -[-2.62,-2.62,2.91,2.91];

plot(sx,sy,'.r','MarkerSize',15);
hold on 
plot(dx,dy,'.b','MarkerSize',15);
plot(-100,100,'k');
rectangle('Position',[-1 -1 2 2]);
hold off
daspect([1 1 1]);
axis([-5, 5, -3, 3]);
legend('Source','Detector','Phantom');

% Fittest 465
% x: [-.01,.01,.02,-.02,.0,-.0,.0,3.09,-3.09,2.45,-2.45,];
% y: [-.22,-.22,-.15,-.15,-.13,-.13,-.08,-2.62,-2.62,2.91,2.91,];
% 25.04