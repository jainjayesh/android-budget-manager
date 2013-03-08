package de.zainodis.balancemanager.model.options;

public abstract class Option<T> {

   private T value;

   public Option() {
   }

   public Option(String value) {
	 this.value = parse(value);
   }

   public Option(T value) {
	 this.value = value;
   }

   public T getValue() {
	 return value;
   }

   protected void setValue(T value) {
	 this.value = value;
   }

   public abstract void execute();

   protected abstract T parse(String value);

   public abstract String getOptionName();

   public abstract String format();

}
