package ml.gorlem.scriptit.widgets;

import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;

import java.util.List;
import java.util.function.Consumer;

public class DropDownButtonWidget extends AbstractPressableButtonWidget {
    private List<Option> options;
    private boolean showingDropDown = false;

    public DropDownButtonWidget(int int_1, int int_2, int int_3, int int_4, String string_1) {
        super(int_1, int_2, int_3, int_4, string_1);
    }

    public void setOptions(List<Option> options) {
        this.options = options;

        for (int i = 0; i < options.size(); i++) {
            Option option = options.get(i);

            option.x = x;
            option.y = y - (i * 21 + 25);
            option.setWidth(width);
            option.visible = false;

            option.parent = this;
        }
    }

    @Override
    public void onPress() {
        showDropDown(!showingDropDown);
    }

    void showDropDown(boolean state) {
        showingDropDown = state;

        for (Option option : options) {
            option.visible = state;
        }
    }

    @Override
    public void render(int int_1, int int_2, float float_1) {
        super.render(int_1, int_2, float_1);

        for (Option option : options) {
            option.render(int_1, int_2, float_1);
        }
    }

    @Override
    public boolean mouseClicked(double double_1, double double_2, int int_1) {
        boolean result = super.mouseClicked(double_1, double_2, int_1);

        if (result)
            return true;

        if (!showingDropDown)
            return false;

        for (Option option : options) {
            result = option.mouseClicked(double_1, double_2, int_1);

            if (result)
                return true;
        }

        return false;
    }

    public static class Option extends AbstractPressableButtonWidget {
        private String text;
        private Consumer<Option> onClick;

        DropDownButtonWidget parent;

        public Option(String text, Consumer<Option> onClick) {
            super(0, 0, 0, 20, text);
            this.text = text;
            this.onClick = onClick;
        }

        @Override
        public void onPress() {
            onClick.accept(this);

            if (parent != null) {
                parent.showDropDown(false);
            }
        }
    }
}
