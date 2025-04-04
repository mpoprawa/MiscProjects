using LinearAlgebra
using JuMP 
using GLPK
using DelimitedFiles

function load(name::String)
    data = []

    open(name, "r") do f
        while !eof(f)
            str = readuntil(f, "\n\n")
            push!(data, readdlm(IOBuffer(str)))
        end
    end
    
    a=vec(data[1])
    b=vec(data[2])
    c=data[3]
    return a,b,c
end

function LP(a::Vector{Float64}, b::Vector{Float64}, c::Matrix{Float64})							
	(m,n)=size(c)
    n=size(a)[1]
    model = Model(GLPK.Optimizer)
	@variable(model, x[1:m, 1:n] >= 0)

    for i in 1:m
        @constraint(model, sum(x[i,j] for j in 1:n) == b[i])
    end

    for i in 1:n
        @constraint(model, sum(x[j,i] for j in 1:m) <= a[i])
    end

    @objective(model, Min, sum(x[i,j]*c[i,j] for i in 1:m for j in 1:n))
    
	print(model)
    optimize!(model)

    println(objective_value(model))
    val = value.(x)
    for i in 1:m
        println(val[i,:])
    end
end

file = "data_z1.txt"

(a,b,c) = load(file)
LP(a, b, c)