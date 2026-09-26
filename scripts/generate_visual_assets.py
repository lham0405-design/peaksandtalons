from pathlib import Path
import struct,zlib
root=Path('src/main/resources/assets/peaksandtalons'); tex=root/'textures/item'; models=root/'models/item'
tex.mkdir(parents=True,exist_ok=True); models.mkdir(parents=True,exist_ok=True)
def blank(): return [[(0,0,0,0) for _ in range(32)] for __ in range(32)]
def px(a,x,y,c):
    if 0<=x<32 and 0<=y<32:a[y][x]=c
def line(a,x0,y0,x1,y1,c,w=1):
    dx=abs(x1-x0);sx=1 if x0<x1 else -1;dy=-abs(y1-y0);sy=1 if y0<y1 else -1;e=dx+dy
    while True:
        for ox in range(-(w//2),w//2+1):
            for oy in range(-(w//2),w//2+1):px(a,x0+ox,y0+oy,c)
        if (x0,y0)==(x1,y1):break
        q=2*e
        if q>=dy:e+=dy;x0+=sx
        if q<=dx:e+=dx;y0+=sy
def out(name,a):
    w=h=32; raw=b''.join(b'\0'+bytes(sum((list(a[y][x]) for x in range(w)),[])) for y in range(h))
    def c(t,d):return struct.pack('>I',len(d))+t+d+struct.pack('>I',zlib.crc32(t+d)&0xffffffff)
    (tex/f'{name}.png').write_bytes(b'\x89PNG\r\n\x1a\n'+c(b'IHDR',struct.pack('>IIBBBBB',w,h,8,6,0,0,0))+c(b'IDAT',zlib.compress(raw,9))+c(b'IEND',b''))
    (models/f'{name}.json').write_text('{\n "parent":"minecraft:item/generated",\n "textures":{"layer0":"peaksandtalons:item/'+name+'"}\n}\n')
# Talonteer: compact diagonal dagger, broad talon-like blade, guard and short wrapped grip.
a=blank(); edge=(35,38,42,255); steel=(173,190,196,255); hi=(239,245,238,255); gold=(190,139,43,255); grip=(78,43,31,255)
line(a,7,7,20,20,edge,5); line(a,8,7,20,19,steel,3); line(a,9,7,19,17,hi,1)
# hooked/talon point
line(a,7,7,6,11,edge,3); line(a,7,8,7,11,steel,2)
# guard perpendicular to blade
line(a,17,22,23,16,gold,3); line(a,19,21,25,27,grip,3); line(a,24,26,26,28,gold,3)
out('talonteer',a)
# Talonspire: long ceremonial spear/relic with split talon head and glowing core.
a=blank(); dark=(38,39,44,255); silver=(177,194,199,255); hi=(235,242,239,255); cyan=(65,217,225,255); gold=(190,139,43,255)
line(a,16,11,16,29,gold,2); line(a,16,3,16,14,dark,5); line(a,16,3,16,13,silver,3); line(a,16,3,16,11,hi,1)
line(a,15,6,8,11,dark,4); line(a,17,6,24,11,dark,4); line(a,15,6,9,10,silver,2); line(a,17,6,23,10,silver,2)
for x,y in [(15,8),(16,8),(17,8),(16,9)]:px(a,x,y,cyan)
out('talonspire',a)
# Detailed chain links: three interlocking oval links for each material.
for name,base,shine in [('iron_chain',(112,123,127,255),(218,226,226,255)),('gold_chain',(184,132,34,255),(255,221,99,255)),('netherite_chain',(63,53,62,255),(137,111,126,255))]:
    a=blank()
    for cx,cy,flip in [(10,9,0),(16,16,1),(22,23,0)]:
        if not flip:
            line(a,cx-3,cy-4,cx+3,cy+4,base,3); line(a,cx-2,cy-4,cx+4,cy+3,shine,1)
        else:
            line(a,cx+3,cy-4,cx-3,cy+4,base,3); line(a,cx+2,cy-4,cx-4,cy+3,shine,1)
    out(name,a)
# Heart of Orduk: dark crimson boss heart with stone/gold fissures.
a=blank(); outline=(47,25,24,255); red=(139,36,32,255); bright=(213,64,49,255); gold=(214,153,55,255)
for y,l,r in [(6,10,14),(6,18,22),(7,8,24),(8,7,25),(9,7,25),(10,7,25),(11,8,24),(12,8,24),(13,9,23),(14,9,23),(15,10,22),(16,10,22),(17,11,21),(18,11,21),(19,12,20),(20,12,20),(21,13,19),(22,14,18),(23,15,17)]:
    for x in range(l,r+1):px(a,x,y,outline if x in (l,r) else red)
line(a,12,8,19,20,bright,2); line(a,19,9,15,15,gold,1); line(a,15,15,18,19,gold,1)
out('heart_of_orduk',a)
