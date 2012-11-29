require 'ruboto/widget'
require 'ruboto/util/stack'

ruboto_import_widgets :Button, :LinearLayout, :TextView

# http://xkcd.com/378/

class SQLDroidActivity
  def on_create(bundle)
    super
    set_title 'Domo arigato, Mr Ruboto!'

    self.content_view =
        linear_layout :orientation => :vertical do
          @text_view = text_view :text => 'What hath Matz wrought?', :id => 42, :width => :match_parent,
                                 :gravity => :center, :text_size => 48.0
          button :text => 'M-x butterfly', :width => :match_parent, :id => 43, :on_click_listener => proc { run_test }
        end
  rescue
    puts "Exception creating activity: #{$!}"
    puts $!.backtrace.join("\n")
  end

  private

  def run_test
    result = 'Failed!'
    with_large_stack(512*1024) do
      load 'db_test.rb'
      result = 'Test run OK!'
    end
    @text_view.text = result
  rescue Exception
    @text_view.text = "Exception: #{$!}"
  end

end
