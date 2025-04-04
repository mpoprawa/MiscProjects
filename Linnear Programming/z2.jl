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
    
    p=vec(data[1])
    t=vec(data[2])
    mach=vec(data[3])
	mat=vec(data[4])
	d=vec(data[5])
	m=data[6]
    return p,t,mach,mat,d,m
end

function LP(p::Vector{Float64}, t::Vector{Float64}, mach::Vector{Float64}, mat::Vector{Float64}, d::Vector{Float64}, m::Matrix{Float64})							
	(i,j) = size(m)
	model = Model(GLPK.Optimizer)
	@variable(model, 0 <= x[1:i])

	@constraint(model, [k = 1:i], x[k] <= d[k])
    @constraint(model, [k = 1:j], sum(m[l,k]*x[l] for l in 1:j) <= t[k]*60)

	@objective(model, Max, sum(x[k]*(p[k] - mat[k] - sum((m[k,l] * mach[l])/60 for l in 1:j)) for k in 1:i))
    
	print(model)
    optimize!(model)
    println(objective_value(model))
    
    val = value.(x)
    println(val)
end

file = "data_z2.txt"
(p,t,mach,mat,d,m) = load(file)
LP(p,t,mach,mat,d,m)