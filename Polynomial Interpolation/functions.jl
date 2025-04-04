#Mikołaj Poprawa
module Functions
using Plots

export ilorazyRoznicowe
export warNewton
export naturalna
export rysujNnfx

#Dane:
#x – wektor długości n + 1 zawierający węzły x0, . . . , xn
#f – wektor długości n + 1 zawierający wartości interpolowanej funkcji w węzłach f (x0), . . . , f (xn)
#Wyniki:
#fx – wektor długości n + 1 zawierający obliczone ilorazy różnicowe
function ilorazyRoznicowe(x::Vector{Float64}, f::Vector{Float64})
    n = length(x)
    fx = Vector{Float64}(undef,n)
    fx[1] = f[1]

    for i in 1:n-1
        for j in 1:n-i
            f[j] = (f[j+1]-f[j])/(x[j+i]-x[j]) 
        end
        fx[i+1] = f[1]
    end
    return fx
end

#Dane:
#x – wektor długości n + 1 zawierający węzły x0, . . . , xn
#fx – wektor długości n + 1 zawierający ilorazy różnicowe
#t – punkt, w którym liczona jest wartość wielomianu
#Wyniki:
#w – wartość wielomianu w punkcie t
function warNewton(x::Vector{Float64}, fx::Vector{Float64}, t::Float64)
    n = length(fx)
    w = fx[n]
    for k in n-1:-1:1
        w = fx[k]+(t-x[k])*w
    end
    return w
end

#Dane:
#x – wektor długości n + 1 zawierający węzły x0, . . . , xn
#fx – wektor długości n + 1 zawierający ilorazy różnicowe
#Wyniki:
#a – wektor długości n + 1 zawierający współczynniki wielomianu interpolacyjnego w postaci naturalnej
function naturalna(x::Vector{Float64}, fx::Vector{Float64})
    n = length(x)
    a = [fx[n]]
    for k in n-1:-1:1
        a = poly_multiply(a,[-x[k],1.0])
        a[1] += fx[k]
    end
    return a
end

#Dane:
#p,q - wektory współczynninków wielomianów
#Wyniki:
#res - wektor współczynników p*q
function poly_multiply(p::Vector{Float64}, q::Vector{Float64})
    p_len = length(p)
    q_len = length(q)
    res = fill(0.0,p_len+q_len-1)
    for i in 1:p_len
        for j in 1:q_len
            res[i+j-1] += p[i]*q[j]
        end
    end
    return res
end

#Dane:
#f – funkcja f(x)
#a,b – przedział interpolacji
#n – stopień wielomianu interpolacyjnego
#Wyniki:
#wykres wileomianu interpolacyjnego i interpolowanej funkcji
function rysujNnfx(f,a::Float64,b::Float64,n::Int)
    x = Vector{Float64}(undef,n+1)
    v = Vector{Float64}(undef,n+1)
    h = (b-a)/n
    for k in 0:n
        x[k+1] = a+k*h
        v[k+1] = f(x[k+1])
    end
    fx = ilorazyRoznicowe(x,v)

    rng = a:0.1:b
    y1 = f.(rng)
    p = plot(t -> warNewton(x,fx,t),a,b)
    plot!(p,rng,y1)
    display(p)
    savefig("plot.png")
end

end