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
    
    k=Int64((data[1])[1,1])
    A=data[2]
    return k,A
end

function LP(k::Int64, A::Matrix{Float64})							
	(m,n) = size(A)
    model = Model(GLPK.Optimizer)
    @variable(model, x[1:m, 1:n], Bin)

    @constraint(model, [i = 1:m, j = 1:n], x[i,j] + A[i,j] <= 1)
    @constraint(model, [i = 1:m, j = 1:n], (sum(x[i+s,j] for s in -k:k if i+s>=1 && i+s <= m) + sum(x[i,j+s] for s in -k:k if j+s>=1 && j+s <= n) - A[i,j])>=0)

    @objective(model, Min, sum(x[k,l] for k in 1:m for l in 1:n))

    #print(model)
    optimize!(model)
    
    println(objective_value(model))
    val = value.(x)
    for i in 1:m
        println(val[i,:])
    end
end

file="data_z6_1.txt"
(k,A)=load(file)
LP(k,A)

file2="data_z6_2.txt"
(k2,A2)=load(file2)
LP(k2,A2)