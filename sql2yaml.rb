#!/usr/bin/env ruby
require 'rubygems'
require 'sqlite3'

if ARGV.length != 1
	puts "Usage: #{$0} filename"
	puts "This tool can be used to convert Ports databases from the format used prior to version 0.3 to the new format. The result is printed to standard output."
	puts "Example: #{$0} /path/to/Ports.db > ports.yml"
	exit
end

db = SQLite3::Database.new(ARGV[0])
rows = db.execute("SELECT * FROM `port`;")

puts "ports:"
rows.each do |row|
	destination = rows.select { |r| r[0] == row[15] }.first
	puts "- ==: net.robinjam.bukkit.ports.persistence.Port"
	puts "  name: \"#{row[1]}\""
	puts "  description: \"#{row[2]}\""
	puts "  world: \"#{row[9]}\""
	puts "  departureSchedule: #{row[16]}"
	puts "  destination: \"#{destination[1]}\"" unless destination.nil?
	puts "  activationRegion:"
	puts "    ==: net.robinjam.bukkit.ports.persistence.PersistentCuboidRegion"
	puts "    x1: #{row[3]}"
	puts "    y1: #{row[4]}"
	puts "    z1: #{row[5]}"
	puts "    x2: #{row[6]}"
	puts "    y2: #{row[7]}"
	puts "    z2: #{row[8]}"
	puts "  arrivalLocation:"
	puts "    ==: net.robinjam.bukkit.ports.persistence.PersistentLocation"
	puts "    x: #{row[10]}"
	puts "    y: #{row[11]}"
	puts "    z: #{row[12]}"
	puts "    yaw: #{row[13]}"
	puts "    pitch: #{row[14]}"
end
