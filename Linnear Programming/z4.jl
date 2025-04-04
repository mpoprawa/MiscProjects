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
    
    n=Int64((data[1])[1,1])
    s=Int64((data[2])[1,1])
    t=Int64((data[3])[1,1])
    T=(data[4])[1,1]
    A=data[5]
    return n,s,t,T,A
end

function LP(n::Int64, s::Int64, t::Int64, T::Float64, A::Matrix{Float64})							
	a = size(A)[1]
    b = zeros(1,n)
    b[s] = 1
    b[t] = -1
    G = fill([0.0,0.0],n,n)
    for i in 1:a
        row = A[i,:]
        G[Int64(row[1]),Int64(row[2])]=[row[3],row[4]]
    end
    model = Model(GLPK.Optimizer)
    @variable(model, x[1:n, 1:n], Bin)

    @constraint(model, [i = 1:n, j = 1:n; G[i, j] == [0.0, 0.0]], x[i, j] == 0)
    @constraint(model, [i = 1:n], sum(x[i, :]) - sum(x[:, i]) == b[i])
    @constraint(model, sum(x[i,j]*G[i,j][2] for i in 1:n for j in 1:n) <= T)

    @objective(model, Min, sum(x[i,j]*G[i,j][1] for i in 1:n for j in 1:n))

    #print(model)
    optimize!(model)
    
    println(objective_value(model))
    val = value.(x)
    for i in 1:n
        for j in 1:n
            if val[i,j]!=0.0
                println(i," ",j," ",G[i,j])
            end
        end
    end
end

file="data_z4_1.txt"
(n,s,t,T,A)=load(file)
LP(n,s,t,T,A)

file2="data_z4_2.txt"
(n2,s2,t2,T2,A2)=load(file2)
LP(n2,s2,t2,T2,A2) #najkrótsza ścieżka 1->2->3->10