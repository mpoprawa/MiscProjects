#Mikołaj Poprawa
include("functions.jl")
using .Functions

function f1(x::Float64)
    return sin(x)
end

x = [-4.0,-2.0,0.0,2.0,4.0]
f = [17.0,5.0,1.0,5.0,17.0]
fx = ilorazyRoznicowe(x,f)
println(fx)
println(warNewton(x,fx,3.0))
println(warNewton(x,fx,-3.5))
println(naturalna(x,fx),"\n")

x = [0.0, 0.4*Float64(pi), 0.8*Float64(pi), 1.2*Float64(pi), 2*Float64(pi)]
f = [sin(0.0), sin(0.4*Float64(pi)), sin(0.8*Float64(pi)), sin(1.2*Float64(pi)), sin(2*Float64(pi))]
fx = ilorazyRoznicowe(x,f)
println(fx)
println(warNewton(x,fx,1.0))
println(warNewton(x,fx,3.0))
println(naturalna(x,fx))

rysujNnfx(f1,-2*Float64(pi),2*Float64(pi),3)
readline()
rysujNnfx(f1,-2*Float64(pi),2*Float64(pi),5)
readline()
rysujNnfx(f1,-2*Float64(pi),2*Float64(pi),7)
readline()
rysujNnfx(f1,-2*Float64(pi),2*Float64(pi),9)
readline()