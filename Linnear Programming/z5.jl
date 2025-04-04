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
    mn=data[3]
	mx=data[4]
    return a,b,mn,mx
end

function LP(a::Vector{Float64}, b::Vector{Float64}, mn::Matrix{Float64}, mx::Matrix{Float64})							
	(m,n) = size(mn)
    model = Model(GLPK.Optimizer)
    @variable(model, x[1:m, 1:n])

    @constraint(model, [k = 1:n], sum(x[l,k] for l in 1:m) >= a[k])
    @constraint(model, [k = 1:m], sum(x[k,l] for l in 1:n) >= b[k])
    @constraint(model, [k = 1:m, l = 1:n], x[k,l] >= mn[k,l])
    @constraint(model, [k = 1:m, l = 1:n], x[k,l] <= mx[k,l])

    @objective(model, Min, sum(x[k,l] for k in 1:m for l in 1:n))

    print(model)
    optimize!(model)
    
    println(objective_value(model))
    val = value.(x)
    for i in 1:m
        println(val[i,:])
    end
end

file="data_z5.txt"
(a,b,mx,mn)=load(file)
LP(a,b,mx,mn)