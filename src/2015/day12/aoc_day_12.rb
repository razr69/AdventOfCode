#!usr/bin/env ruby

require 'json'

def get_sum val, get_red = true
  val.is_a?(Integer) ? val :
    (val.is_a?(Array) ? val.reduce(0) { |x, e| x + get_sum(e, get_red) } :
      (val.is_a?(Hash) && (get_red || !val.has_value?('red')) ? val.reduce(0) { |x, e| x + get_sum(e[1], get_red) } : 0))
end

if ARGV[0] && File.file?(ARGV[0])
  input_file = ARGV[0]
else
  puts 'either no argument given or argument is not a file'
  exit 1
end

input = JSON.parse File.read(input_file)

puts "The sum of all numbers of the given input is #{get_sum input}."
puts "The sum of all numbers without those inside a red object is #{get_sum input, false}."

exit 0
