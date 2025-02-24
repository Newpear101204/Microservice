package com.lehoangtan.borrowingservice.command.api.saga;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.lehoangtan.borrowingservice.command.api.command.DeleteBorrowCommand;
import com.lehoangtan.borrowingservice.command.api.command.SendMessageCommand;
import com.lehoangtan.borrowingservice.command.api.events.BorrowCreatedEvent;
import com.lehoangtan.borrowingservice.command.api.events.BorrowDeletedEvent;
import com.lehoangtan.borrowingservice.command.api.events.BorrowingUpdateBookReturnEvent;
import com.lehoangtan.commonservice.command.RollBackStatusBookCommand;
import com.lehoangtan.commonservice.command.UpdateStatusBookCommand;
import com.lehoangtan.commonservice.events.BookRollBackStatusEvent;
import com.lehoangtan.commonservice.events.BookUpdateStatusEvent;
import com.lehoangtan.commonservice.model.BookResponseCommonModel;
import com.lehoangtan.commonservice.model.EmployeeResponseCommonModel;
import com.lehoangtan.commonservice.query.GetDetailsBookQuery;
import com.lehoangtan.commonservice.query.GetDetailsEmployeeQuery;


@Saga
public class BorrowingSaga {
	@Autowired 
	private transient CommandGateway commandGateway;
	 
	 @Autowired 
	 private transient QueryGateway queryGateway;
	 
	 @StartSaga
	 @SagaEventHandler(associationProperty = "id")
	 private void handle(BorrowCreatedEvent event) {
		 System.out.println("BorrowCreatedEvent in Saga for BookId : "+event.getBookId()+ " : EmployeeId :  "+event.getEmployeeId());
	    	
    	 try {
    		 SagaLifecycle.associateWith("bookId", event.getBookId());
    		 
    		 GetDetailsBookQuery getDetailsBookQuery = new GetDetailsBookQuery(event.getBookId());
    		
    		 BookResponseCommonModel bookResponseModel =
    			        queryGateway.query(getDetailsBookQuery,
    			                ResponseTypes.instanceOf(BookResponseCommonModel.class))
    			                .join();
    		 if(bookResponseModel.getIsReady() == true) {
    			 UpdateStatusBookCommand command = new UpdateStatusBookCommand(event.getBookId(), false,event.getEmployeeId(),event.getId());
    			 commandGateway.sendAndWait(command);
    		 }
    		 else {
    			 
    			 throw new Exception("Sach Da co nguoi Muon");
    		 }
    		 
    		
		} catch (Exception e) {
			rollBackBorrowRecord(event.getId());
			
			System.out.println(e.getMessage());
		}
	 }
	 
	 @SagaEventHandler(associationProperty = "bookId")
	    private void handle(BookUpdateStatusEvent event) {
	    	 System.out.println("BookUpdateStatusEvent in Saga for BookId : "+event.getBookId());
	    	 try {
				GetDetailsEmployeeQuery getDetailsEmployeeQuery = new GetDetailsEmployeeQuery(event.getEmployeeId());
				
				 EmployeeResponseCommonModel employeeResponseCommonModel =
	 			        queryGateway.query(getDetailsEmployeeQuery,
	 			                ResponseTypes.instanceOf(EmployeeResponseCommonModel.class))
	 			                .join();
				 if(employeeResponseCommonModel.getIsDisciplined()==true) {
					 throw new Exception("Nhan vien bi ky luat");
				 }else {
					 commandGateway.sendAndWait(new SendMessageCommand(event.getBorrowId(), event.getEmployeeId(), "Da muon sach thanh cong !"));
					 SagaLifecycle.end();
				 }
			} catch (Exception e) {
				
				System.out.println(e.getMessage());
				rollBackBookStatus(event.getBookId(),event.getEmployeeId(),event.getBorrowId());
			}
	    }
	 
	 @SagaEventHandler(associationProperty = "bookId")
	    public void handleRollBackBookStatus(BookRollBackStatusEvent event) {
	    	System.out.println("BookRollBackStatusEvent in Saga for book Id : {} " + event.getBookId());
	    	rollBackBorrowRecord(event.getBorrowId());
	    }
	 
	 @SagaEventHandler(associationProperty = "id")
	    @EndSaga
	    public void handle(BorrowDeletedEvent event) {
	    	System.out.println("BorrowDeletedEvent in Saga for Borrowing Id : {} " +
	                event.getId());
	    	SagaLifecycle.end();
	    }
	 
	 @StartSaga
	 @SagaEventHandler(associationProperty = "id")
	 public void handle(BorrowingUpdateBookReturnEvent event) {
		 System.out.println("BorrowingUpdateBookReturnEvent in Saga for borrowing Id : "+event.getId());
		 try {
			 UpdateStatusBookCommand updateStatusBookCommand = new UpdateStatusBookCommand(event.getBookId(), true, event.getEmployee(), event.getId());
			 commandGateway.sendAndWait(updateStatusBookCommand);
			 commandGateway.sendAndWait(new SendMessageCommand(event.getId(), event.getEmployee(), "Da tra sach thanh cong !"));
				SagaLifecycle.end();
			 
		 }catch( Exception e) {
			 System.out.print(e.getMessage());
		 }
	 }
	 
	 
	 
	 
	  private void rollBackBorrowRecord(String id) {
	    	commandGateway.sendAndWait(new DeleteBorrowCommand(id));
	    }
	  private void rollBackBookStatus(String bookId,String employeeId,String borrowId) {
	    	SagaLifecycle.associateWith("bookId", bookId);
	    	RollBackStatusBookCommand command = new RollBackStatusBookCommand(bookId, true,employeeId,borrowId);
			 commandGateway.sendAndWait(command);
	    }
	 
}