hook.add("PlayerDeath", "worldMain", function (ply)
	ply:playSound("random.drink", 1, 0.9)
	print("PlayedSound \"random.drink\"")
end)