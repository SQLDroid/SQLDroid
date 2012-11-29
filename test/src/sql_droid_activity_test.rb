activity Java::org.sqldroid.SQLDroidActivity

setup do |activity|
  start = Time.now
  loop do
    @text_view = activity.findViewById(42)
    break if @text_view || (Time.now - start > 60)
    sleep 1
  end
  assert @text_view
end

test('General DB test') do |activity|
  button = activity.findViewById(43)
  button.performClick
  assert_equal 'Test run OK!', @text_view.text
end
