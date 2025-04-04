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
    
    c=vec(data[1])
    a=vec(data[2])
    o=vec(data[3])
	d=vec(data[4])
	p=vec(data[5])
	s=vec(data[6])
    m=vec(data[7])
    st=(data[8][1,1])
    return c,a,o,d,p,s,m,st
end

function LP(c::Vector{Float64},a::Vector{Float64},o::Vector{Float64},d::Vector{Float64},p::Vector{Float64},s::Vector{Float64},m::Vector{Float64},st::Float64)							
	k = size(c)[1]
	model = Model(GLPK.Optimizer)
	
    @variable(model, 0 <= x[1:k])
    @variable(model, 0 <= y[1:k])
    @variable(model, 0 <= z[0:k])
    fix(z[0], st; force=true)

    @constraint(model, [i = 1:k], x[i] <= p[i])
    @constraint(model, [i = 1:k], y[i] <= a[i])
    @constraint(model, [i = 1:k], z[i] <= s[i])
    @constraint(model, [i = 1:k], z[i-1] + x[i] + y[i] - d[i] == z[i])

    @objective(model, Min, sum(x[i]*c[i]+y[i]*o[i]+z[i]*m[i] for i in 1:k))
    
    print(model)
    optimize!(model)
    println(objective_value(model))
    
    val_x = value.(x)
    val_y = value.(y)
    val_z = value.(z)
    println(val_x)
    println(val_y)
    println(val_z)
end

file="data_z3.txt"
(c,a,o,d,p,s,m,st)=load(file)
LP(c,a,o,d,p,s,m,st)