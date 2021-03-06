clear;

sx = [.0,-.46,-.76,1.27,.46,.76,-1.27];
sy = -[1.53,-.58,-.1,.2,-.58,-.1,.2];

dx = [-2.19,1.63,2.19,-1.63];
dy = -[2.16,-2.12,2.16,-2.12];

plot(sx,sy,'.r','MarkerSize',15);
hold on 
plot(dx,dy,'.b','MarkerSize',15);
plot(-100,100,'k');
rectangle('Position',[-1 -1 2 2]);
hold off
daspect([1 1 1]);
axis([-5, 5, -5, 5]);
legend('Source','Detector','Phantom');

% Fittest 351
% x: [.0,-.46,-.76,1.27,-2.19,1.63,.0,.0,.0,.0,.0,];
% y: [1.53,-.58,-.1,.2,2.16,-2.12,.0,.0,.0,.0,.0,];
% 19.94
